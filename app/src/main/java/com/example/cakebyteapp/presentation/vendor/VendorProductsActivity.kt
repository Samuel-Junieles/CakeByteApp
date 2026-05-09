package com.example.cakebyteapp.presentation.vendor

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityVendorProductsBinding
import com.example.cakebyteapp.presentation.admin.AdminViewModel
import com.example.cakebyteapp.presentation.admin.ProductsViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private val adminViewModel: AdminViewModel by viewModels() // Reusamos el logout del adminViewModel o crea uno VendorViewModel
    private lateinit var adapter: VendorProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = VendorProductAdapter(
            onEdit = { product ->
                val intent = Intent(this, AddEditProductActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        )
        binding.rvVendorProducts.apply {
            layoutManager = GridLayoutManager(this@VendorProductsActivity, 2)
            adapter = this@VendorProductsActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.btnAddProduct.setOnClickListener {
            val intent = Intent(this, AddEditProductActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        binding.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(0, 0)
        }

        binding.bottomNavigation.selectedItemId = R.id.navigation_vendor_home
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_vendor_home -> true
                R.id.navigation_vendor_orders -> {
                    // Navegar a pedidos
                    true
                }
                R.id.navigation_vendor_profile -> {
                    adminViewModel.logout()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.navigation_vendor_home

        binding.tabCategories.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = when (tab?.position) {
                    1 -> "Pan"
                    2 -> "Pasteles"
                    3 -> "Galletas"
                    else -> "Todos"
                }
                viewModel.onCategoryFilterChanged(category)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            adminViewModel.logoutSuccess.collect { success ->
                if (success) {
                    val intent = Intent(this@VendorProductsActivity, com.example.cakebyteapp.LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.products.collect { productList ->
                    adapter.submitList(productList)
                }
            }
        }
    }
}
