package com.example.cakebyteapp.presentation.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityAdminUsersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUsersBinding
    private val viewModel: UsersViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(
            onEdit = { user -> Toast.makeText(this, "Editar: ${user.name}", Toast.LENGTH_SHORT).show() },
            onDelete = { user -> viewModel.deleteUser(user) }
        )
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@AdminUsersActivity)
            adapter = this@AdminUsersActivity.adapter
        }
    }

    private fun setupListeners() {
        // Buscador
        binding.etSearch.addTextChangedListener { text ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }

        // Filtros de Rol
        binding.cgFilters.setOnCheckedStateChangeListener { _, checkedIds ->
            val role = when (checkedIds.firstOrNull()) {
                R.id.chipAdmin -> "Admin"
                R.id.chipVendor -> "Vendedor"
                else -> "Todos"
            }
            viewModel.onRoleFilterChanged(role)
        }

        // Navegación Inferior
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    finish()
                    true
                }
                R.id.navigation_users -> true
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.navigation_users

        // Botón Crear Usuario
        binding.btnCreateUser.setOnClickListener {
            val intent = Intent(this@AdminUsersActivity, CreateUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.users.collect { userList ->
                    adapter.submitList(userList)
                }
            }
        }
    }
}