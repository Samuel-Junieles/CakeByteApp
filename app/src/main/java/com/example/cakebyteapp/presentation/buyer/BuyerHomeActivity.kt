package com.example.cakebyteapp.presentation.buyer

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityBuyerHomeBinding
import com.example.cakebyteapp.presentation.admin.ProductsViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BuyerHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyerHomeBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var adapter: BuyerProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = BuyerProductAdapter { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            startActivity(intent)
        }
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(this@BuyerHomeActivity, 2)
            adapter = this@BuyerHomeActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
        
        binding.etSearch.addTextChangedListener { text ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }

        binding.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.tabCategories.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = when (tab?.position) {
                    0 -> "Todos"
                    1 -> "Bebidas"
                    2 -> "Pan"
                    3 -> "Pasteles"
                    4 -> "Galletas"
                    else -> "Todos"
                }
                viewModel.onCategoryFilterChanged(category)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        
        // Seleccionar "Todos" por defecto (pos 0)
        binding.tabCategories.post {
            binding.tabCategories.getTabAt(0)?.select()
        }

        binding.bottomNavigation.selectedItemId = R.id.navigation_home
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
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
                viewModel.products.collect { productList ->
                    adapter.submitList(productList)
                }
            }
        }
    }
}
