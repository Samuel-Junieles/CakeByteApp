package com.example.cakebyteapp.presentation.buyer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cakebyteapp.databinding.ActivityProductDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            viewModel.loadProduct(productId)
        }

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
        
        binding.btnSize16.setOnClickListener { viewModel.selectSize("16 cm") }
        binding.btnSize20.setOnClickListener { viewModel.selectSize("20 cm") }
        binding.btnSize22.setOnClickListener { viewModel.selectSize("22 cm") }
        binding.btnSize24.setOnClickListener { viewModel.selectSize("24 cm") }

        binding.btnAddToCart.setOnClickListener {
            val note = binding.etNote.text.toString()
            viewModel.addToCart(note)
            Toast.makeText(this, "Añadido al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.product.collect { product ->
                product?.let {
                    binding.tvProductName.text = it.name
                    binding.tvProductPrice.text = "$${it.price}"
                    binding.tvTotalPrice.text = "$${it.price}"
                    binding.tvDescription.text = it.description
                    binding.tvAllergens.text = it.allergens
                    binding.tvRating.text = "${it.rating} (${it.reviewCount})"
                }
            }
        }

        lifecycleScope.launch {
            viewModel.selectedSize.collect { size ->
                updateSizeButtons(size)
            }
        }
    }

    private fun updateSizeButtons(selectedSize: String) {
        val buttons = listOf(binding.btnSize16, binding.btnSize20, binding.btnSize22, binding.btnSize24)
        buttons.forEach { button ->
            if (button.text == selectedSize) {
                button.setBackgroundColor(getColor(com.example.cakebyteapp.R.color.salmon_primary))
                button.setTextColor(getColor(com.example.cakebyteapp.R.color.white))
            } else {
                button.setBackgroundColor(getColor(android.R.color.transparent))
                button.setTextColor(getColor(com.example.cakebyteapp.R.color.black))
            }
        }
    }
}
