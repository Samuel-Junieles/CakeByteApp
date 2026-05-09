package com.example.cakebyteapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cakebyteapp.databinding.ActivityAdminDashboardBinding
import com.example.cakebyteapp.presentation.admin.AdminReportsActivity
import com.example.cakebyteapp.presentation.admin.AdminProductsActivity
import com.example.cakebyteapp.presentation.admin.AdminUsersActivity
import com.example.cakebyteapp.presentation.auth.UserProfileActivity
import com.example.cakebyteapp.presentation.admin.AdminViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private val viewModel: AdminViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.logoutSuccess.collect { success ->
                if (success) {
                    val intent = Intent(this@AdminDashboardActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.navigation_dashboard
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> true
                R.id.navigation_users -> {
                    startActivity(Intent(this, AdminUsersActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
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
    }
}
