package com.example.cakebyteapp.presentation.vendor

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityAddEditProductBinding
import com.example.cakebyteapp.presentation.admin.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditProductBinding
    private val viewModel: ProductsViewModel by viewModels()
    private var productId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getIntExtra("PRODUCT_ID", 0)
        
        setupDropdown()
        setupListeners()
        
        if (productId != 0) {
            binding.tvTitle.text = getString(R.string.title_edit_product)
            loadProductData()
        }
    }

    private fun setupDropdown() {
        val categories = arrayOf("Pan", "Pasteles", "Galletas", "Tartas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.actvCategory.setAdapter(adapter)
    }

    private fun loadProductData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getProductById(productId).collect { product ->
                    product?.let {
                        binding.etName.setText(it.name)
                        binding.etDesc.setText(it.description)
                        binding.etPrice.setText(it.price.toString())
                        binding.actvCategory.setText(it.category, false)
                        binding.etStock.setText(it.stock.toString())
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { 
            finish()
            overridePendingTransition(0, 0)
        }
        binding.btnCancel.setOnClickListener { 
            finish()
            overridePendingTransition(0, 0)
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val desc = binding.etDesc.text.toString()
            val price = binding.etPrice.text.toString().toDoubleOrNull() ?: 0.0
            val category = binding.actvCategory.text.toString()
            val stock = binding.etStock.text.toString().toIntOrNull() ?: 0

            if (name.isBlank() || price <= 0) {
                Toast.makeText(this, "El nombre y el precio son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveProduct(
                id = productId,
                name = name,
                description = desc,
                price = price,
                category = category,
                stock = stock,
                status = "Activo"
            )
            
            Toast.makeText(this, "Producto publicado con éxito", Toast.LENGTH_SHORT).show()
            finish()
            overridePendingTransition(0, 0)
        }

        binding.bottomNavigation.selectedItemId = R.id.navigation_vendor_home
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_vendor_home -> {
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
}
