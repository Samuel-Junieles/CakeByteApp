package com.example.cakebyteapp.domain.usecase

import com.example.cakebyteapp.data.local.entity.UserEntity
import com.example.cakebyteapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun getInitialDestination(): Destination {
        val user = repository.getCurrentUser().first()
        return when {
            user == null -> Destination.Login
            else -> when (user.role) {
                "Admin" -> Destination.AdminDashboard
                "Vendedor" -> Destination.VendedorDashboard
                else -> Destination.CompradorDashboard
            }
        }
    }

    sealed class Destination {
        object Login : Destination()
        object AdminDashboard : Destination()
        object VendedorDashboard : Destination()
        object CompradorDashboard : Destination()
    }
}