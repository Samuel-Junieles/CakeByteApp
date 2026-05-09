package com.example.cakebyteapp.presentation.admin

import android.content.Intent
import android.os.Bundle
import com.example.cakebyteapp.AdminDashboardActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityAdminUsersBinding
import com.example.cakebyteapp.presentation.auth.UserProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUsersBinding
    private val viewModel: UsersViewModel by viewModels()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter(
            onEdit = { user -> 
                val intent = Intent(this, CreateUserActivity::class.java)
                intent.putExtra("USER_EMAIL", user.email)
                startActivity(intent)
            },
            onDelete = { user -> viewModel.deleteUser(user) }
        )
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(this@AdminUsersActivity)
            adapter = this@AdminUsersActivity.adapter
        }
    }

    private fun setupListeners() {
        binding.etSearch.addTextChangedListener { text ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }

        binding.cgFilters.setOnCheckedStateChangeListener { _, checkedIds ->
            val role = when (checkedIds.firstOrNull()) {
                R.id.chipAdmin -> "Admin"
                R.id.chipVendor -> "Vendedor"
                else -> "Todos"
            }
            viewModel.onRoleFilterChanged(role)
        }

        binding.bottomNavigation.selectedItemId = R.id.navigation_users
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    startActivity(Intent(this, AdminDashboardActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_users -> true
                R.id.navigation_products -> {
                    startActivity(Intent(this, AdminProductsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_reports -> {
                    startActivity(Intent(this, AdminReportsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, UserProfileActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    true
                }
                else -> false
            }
        }

        binding.btnCreateUser.setOnClickListener {
            startActivity(Intent(this, CreateUserActivity::class.java))
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
