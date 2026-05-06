package com.example.cakebyteapp.presentation.admin

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityCreateUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateUserBinding
    private val viewModel: CreateUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDropdown()
        setupListeners()
        observeViewModel()
    }



    private fun setupDropdown() {
        val roles = arrayOf("Admin", "Vendedor", "Comprador")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        binding.actvRole.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val surname = binding.etSurname.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val role = binding.actvRole.text.toString()

            viewModel.saveUser(name, surname, email, phone, role)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        // Importante: Primero establecemos el ID seleccionado y LUEGO el listener.
        // Si se hace al revés, el listener se dispara durante la inicialización
        // y ejecuta el finish() inmediatamente, cerrando la pantalla nada más abrirse.
        binding.bottomNavigation.selectedItemId = R.id.navigation_users
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_users -> {
                    // Volver a la lista de usuarios (que ya está debajo en el stack)
                    finish()
                    true
                }
                R.id.navigation_dashboard -> {
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is CreateUserViewModel.CreateUserUiState.Loading -> {
                            binding.btnSave.isEnabled = false
                        }
                        is CreateUserViewModel.CreateUserUiState.Error -> {
                            binding.btnSave.isEnabled = true
                            Toast.makeText(this@CreateUserActivity, state.message, Toast.LENGTH_LONG).show()
                        }
                        is CreateUserViewModel.CreateUserUiState.Success -> {
                            Toast.makeText(this@CreateUserActivity, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            binding.btnSave.isEnabled = true
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect { event ->
                    when (event) {
                        is CreateUserViewModel.CreateUserNavigationEvent.NavigateBack -> finish()
                    }
                }
            }
        }
    }
}