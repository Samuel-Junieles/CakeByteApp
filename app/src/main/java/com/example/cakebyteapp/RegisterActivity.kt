package com.example.cakebyteapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.cakebyteapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvGoToLogin.setOnClickListener {
            finish() // Volver a la pantalla de login (que está debajo en el stack)
        }

        binding.btnSignup.setOnClickListener {
            // Lógica de registro
        }

        binding.btnGoogleSignUp.setOnClickListener {
            // Lógica de registro con Google
        }
    }
}