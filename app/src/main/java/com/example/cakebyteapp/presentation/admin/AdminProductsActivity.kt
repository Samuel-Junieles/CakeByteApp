package com.example.cakebyteapp.presentation.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cakebyteapp.AdminDashboardActivity
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityAdminProductsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        // Para pruebas iniciales, cargar datos de ejemplo si la lista está vacía
        viewModel.addSampleProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            onEdit = { product -> Toast.makeText(this, "Editar: ${product.name}", Toast.LENGTH_SHORT).show() }
        )
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(this@AdminProductsActivity)
            adapter = this@AdminProductsActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }

        binding.cgFilters.setOnCheckedStateChangeListener { _, checkedIds ->
            val status = when (checkedIds.firstOrNull()) {
                R.id.chipActive -> "Activo"
                R.id.chipSuspended -> "Suspendido"
                else -> "Todos"
            }
            viewModel.onStatusFilterChanged(status)
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    val intent = Intent(this, AdminDashboardActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.navigation_users -> {
                    val intent = Intent(this, AdminUsersActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()

                    true
                }
                R.id.navigation_products -> true
                R.id.navigation_reports -> {
                    val intent = Intent(this, AdminReportsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.navigation_products
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
