package com.example.cakebyteapp.presentation.vendor

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cakebyteapp.LoginActivity
import com.example.cakebyteapp.R
import com.example.cakebyteapp.databinding.ActivityVendorProfileBinding
import com.example.cakebyteapp.presentation.admin.AdminViewModel
import com.example.cakebyteapp.domain.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VendorProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVendorProfileBinding
    private val viewModel: AdminViewModel by viewModels() // Reusamos logout
    
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVendorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
        loadUserData()
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            val user = authRepository.getCurrentUser().first()
            user?.let {
                binding.tvName.text = it.name
                binding.tvEmail.text = it.email
            }
        }
    }

    private fun setupListeners() {
        binding.bottomNavigation.selectedItemId = R.id.navigation_vendor_profile
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_vendor_home -> {
                    startActivity(Intent(this, VendorProductsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.navigation_vendor_orders -> {
                    startActivity(Intent(this, VendorOrdersActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.navigation_vendor_profile -> true
                else -> false
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.logoutSuccess.collect { success ->
                if (success) {
                    val intent = Intent(this@VendorProfileActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
