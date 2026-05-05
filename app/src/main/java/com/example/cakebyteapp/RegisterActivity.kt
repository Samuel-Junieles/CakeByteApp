package com.example.cakebyteapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.cakebyteapp.databinding.ActivityRegisterBinding
import com.example.cakebyteapp.presentation.register.RegisterState
import com.example.cakebyteapp.presentation.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRoleSpinner()
        observeState()

        binding.tvGoToLogin.setOnClickListener { finish() }

        binding.btnSignup.setOnClickListener {
            val name = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            val role = binding.spinnerRole.selectedItem.toString()

            viewModel.register(email, pass, name, role)
        }
    }

    private fun setupRoleSpinner() {
        val roles = listOf("Comprador", "Vendedor", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        binding.spinnerRole.adapter = adapter
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is RegisterState.Loading -> binding.btnSignup.isEnabled = false
                    is RegisterState.Success -> {
                        Toast.makeText(this@RegisterActivity, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()
                        finish() // Vuelve al login
                    }
                    is RegisterState.Error -> {
                        binding.btnSignup.isEnabled = true
                        Toast.makeText(this@RegisterActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                    else -> binding.btnSignup.isEnabled = true
                }
            }
        }
    }
}