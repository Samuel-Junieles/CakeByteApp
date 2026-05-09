package com.example.cakebyteapp.presentation.buyer

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityCartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val viewModel: CartViewModel by viewModels()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = CartAdapter(
            onUpdateQuantity = { item, newQuantity -> viewModel.updateQuantity(item, newQuantity) },
            onRemove = { item -> viewModel.removeItem(item) }
        )
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = this@CartActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.btnCheckout.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }

        binding.bottomNavigation.selectedItemId = R.id.navigation_cart
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    finish()
                    true
                }
                R.id.navigation_cart -> true
                R.id.navigation_profile -> {
                    startActivity(Intent(this, com.example.cakebyteapp.presentation.auth.UserProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartItems.collect { items ->
                    adapter.submitList(items)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.totalPrice.collect { total ->
                    val colombianLocale = Locale("es", "CO")
                    val currencyFormatter = NumberFormat.getCurrencyInstance(colombianLocale)
                    binding.tvSubtotalPrice.text = currencyFormatter.format(total)
                    binding.tvTotalPrice.text = currencyFormatter.format(total)
                }
            }
        }
    }
}
