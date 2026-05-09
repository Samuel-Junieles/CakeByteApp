package com.example.cakebyteapp.presentation.buyer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cakebyteapp.databinding.ActivityCheckoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val viewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnConfirmOrder.setOnClickListener {
            // Simular proceso de pago
            startActivity(Intent(this, PaymentSuccessActivity::class.java))
            finish()
        }
    }
}
