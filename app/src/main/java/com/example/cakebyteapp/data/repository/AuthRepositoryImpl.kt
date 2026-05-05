package com.example.cakebyteapp.data.repository

import com.example.cakebyteapp.data.local.dao.AuthDao
import com.example.cakebyteapp.data.local.entity.UserEntity
import com.example.cakebyteapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDao: AuthDao
) : AuthRepository {
    
    override suspend fun login(email: String, pass: String): Result<UserEntity> {
        val user = authDao.validateCredentials(email, pass)
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Credenciales incorrectas"))
        }
    }

    override suspend fun register(email: String, pass: String, name: String, role: String): Result<Unit> {
        return try {
            val user = UserEntity(email = email, password = pass, name = name, role = role)
            authDao.insertUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<UserEntity?> {
        return authDao.getCurrentUser()
    }

    override suspend fun logout(): Result<Unit> {
        // En una app real con tokens, aquí los invalidarías.
        // Para este prototipo local, simplemente retornamos éxito.
        return Result.success(Unit)
    }
}