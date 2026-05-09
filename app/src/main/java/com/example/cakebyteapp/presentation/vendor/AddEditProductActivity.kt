package com.example.cakebyteapp.presentation.vendor

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityAddEditProductBinding
import com.example.cakebyteapp.presentation.admin.ProductsViewModel
import com.example.cakebyteapp.presentation.auth.UserProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditProductBinding
    private val viewModel: ProductsViewModel by viewModels()
    private var productId: Int = 0
    private var selectedImageName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getIntExtra("PRODUCT_ID", 0)
        
        setupDropdown()
        setupListeners()
        
        if (productId != 0) {
            binding.tvTitle.text = "Editar Producto"
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
                        selectedImageName = it.imageUrl ?: ""
                        updatePreviewImage(selectedImageName)
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener { finish() }

        binding.imagePickerCard.setOnClickListener {
            showImagePickerDialog()
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
                status = "Activo",
                imageUrl = selectedImageName
            )
            
            Toast.makeText(this, "Producto guardado con éxito", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.bottomNavigation.selectedItemId = R.id.navigation_vendor_home
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_vendor_home -> {
                    finish()
                    true
                }
                R.id.navigation_vendor_profile -> {
                    startActivity(Intent(this, UserProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun showImagePickerDialog() {
        val images = arrayOf(
            "Torta de Chocolate", "Torta de Vainilla", "Red Velvet", 
            "Carrot Cake", "Cheesecake Mora", "Cheesecake Fresa", "Galletas"
        )
        val imageNames = arrayOf(
            "torta_de_chocolate", "torta_de_vainilla", "red_velvet_torta", 
            "carrot_torta", "cheesecake_de_mora", "cheesecake_de_fresa", "galletas_de_chocolate"
        )

        AlertDialog.Builder(this)
            .setTitle("Seleccionar Imagen")
            .setItems(images) { _, which ->
                selectedImageName = imageNames[which]
                updatePreviewImage(selectedImageName)
            }
            .show()
    }

    private fun updatePreviewImage(name: String) {
        if (name.isEmpty()) return
        val resId = resources.getIdentifier(name, "drawable", packageName)
        if (resId != 0) {
            binding.ivProductPreview.setImageResource(resId)
            binding.ivProductPreview.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
        }
    }
}
