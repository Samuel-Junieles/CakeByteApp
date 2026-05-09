package com.example.cakebyteapp.presentation.vendor

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
import com.example.cakebyteapp.databinding.ActivityVendedorProductsBinding
import com.example.cakebyteapp.presentation.admin.ProductsViewModel
import com.example.cakebyteapp.presentation.auth.UserProfileActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendedorProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var adapter: VendorProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendedorProductsBinding.inflate(layoutInflater)
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
                startActivity(intent)
            }
        )
        binding.rvVendorProducts.apply {
            layoutManager = GridLayoutManager(this@VendorProductsActivity, 2)
            adapter = this@VendorProductsActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
        
        binding.etSearch.addTextChangedListener { text ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }

        binding.btnAddProduct.setOnClickListener {
            startActivity(Intent(this, AddEditProductActivity::class.java))
        }

        binding.bottomNavigation.selectedItemId = R.id.navigation_vendor_home
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_vendor_home -> true
                R.id.navigation_vendor_orders -> {
                    startActivity(Intent(this, VendorOrdersActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    true
                }
                R.id.navigation_vendor_profile -> {
                    startActivity(Intent(this, UserProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    true
                }
                else -> false
            }
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
