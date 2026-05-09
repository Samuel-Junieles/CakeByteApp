package com.example.cakebyteapp.presentation.vendor

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityVendorOrdersBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VendorOrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var adapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
        
        viewModel.addSampleOrders()
    }

    private fun setupRecyclerView() {
        adapter = OrderAdapter(
            onComplete = { order -> viewModel.completeOrder(order) },
            onDelete = { order -> viewModel.deleteOrder(order) }
        )
        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(this@VendorOrdersActivity)
            adapter = this@VendorOrdersActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.bottomNavigation.selectedItemId = R.id.navigation_vendor_orders
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_vendor_home -> {
                    startActivity(Intent(this, VendorProductsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.navigation_vendor_orders -> true
                R.id.navigation_vendor_profile -> {
                    startActivity(Intent(this, VendorProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }

        binding.tabOrderFilters.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val filter = when (tab?.position) {
                    1 -> "Pendiente"
                    2 -> "Entregado"
                    else -> "Todos"
                }
                viewModel.onFilterChanged(filter)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orders.collect { orderList ->
                    adapter.submitList(orderList)
                }
            }
        }
    }
}
