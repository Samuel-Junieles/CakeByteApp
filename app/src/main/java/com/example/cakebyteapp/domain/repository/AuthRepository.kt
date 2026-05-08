package com.example.cakebyteapp.domain.repository

import com.example.cakebyteapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, pass: String): Result<UserEntity>
    suspend fun register(email: String, pass: String, name: String, role: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    fun getCurrentUser(): Flow<UserEntity?>
    fun getAllUsers(): Flow<List<UserEntity>>
}