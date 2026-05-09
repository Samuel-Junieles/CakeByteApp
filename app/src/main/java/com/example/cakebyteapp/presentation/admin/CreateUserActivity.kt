package com.example.cakebyteapp.presentation.admin

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cakebyteapp.databinding.ActivityCreateUserBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateUserBinding
    private val viewModel: CreateUserViewModel by viewModels()
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDropdown()
        setupListeners()
        observeViewModel()
        
        val userEmail = intent.getStringExtra("USER_EMAIL")
        if (userEmail != null) {
            isEditMode = true
            binding.tvTitle.text = "Editar Usuario"
            viewModel.loadUser(userEmail)
        }
    }

    private fun setupDropdown() {
        val roles = arrayOf("Admin", "Vendedor", "Comprador")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        binding.actvRole.setAdapter(adapter)
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener { finish() }
        
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            val surname = binding.etSurname.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val role = binding.actvRole.text.toString()
            
            viewModel.saveUser(name, surname, email, phone, role, isEditMode)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userToEdit.collect { user ->
                    user?.let {
                        val names = it.name.split(" ")
                        binding.etName.setText(names.firstOrNull() ?: "")
                        binding.etSurname.setText(if (names.size > 1) names.drop(1).joinToString(" ") else "")
                        binding.etEmail.setText(it.email)
                        binding.etPhone.setText(it.phone)
                        binding.actvRole.setText(it.role, false)
                        
                        // En edición no permitimos cambiar el correo (es el identificador)
                        binding.etEmail.isEnabled = false
                    }
                }
            }
        }

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
                            Toast.makeText(this@CreateUserActivity, "Operación exitosa", Toast.LENGTH_SHORT).show()
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
                    if (event is CreateUserViewModel.CreateUserNavigationEvent.NavigateBack) {
                        finish()
                    }
                }
            }
        }
    }
}
