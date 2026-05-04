package com.example.cakebyteapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.cakebyteapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            // Lógica de inicio de sesión
            val password = binding.etPassword.text.toString()

            if (password.length < 6) {
                binding.tvErrorPassword.visibility = android.view.View.VISIBLE
            } else {
                binding.tvErrorPassword.visibility = android.view.View.GONE
                // Aquí iría la lógica para navegar a la pantalla principal
            }
        }


        binding.tvForgotPassword.setOnClickListener {
            // Lógica de recuperar contraseña
        }
    }
}