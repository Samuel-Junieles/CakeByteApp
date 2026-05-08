package com.example.cakebyteapp.data.repository

import com.example.cakebyteapp.data.local.dao.AuthDao
import com.example.cakebyteapp.data.local.entity.UserEntity
import com.example.cakebyteapp.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDao: AuthDao,
    private val supabaseClient: SupabaseClient,
) : AuthRepository {
    
    override suspend fun login(email: String, pass: String): Result<UserEntity> {
        return try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = pass
            }
            // Obtenemos el usuario de la DB local o creamos uno temporal
            val user = authDao.validateCredentials(email, pass) 
                ?: UserEntity(email = email, password = pass, name = "Usuario Supabase", role = "Comprador")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, pass: String, name: String, role: String): Result<Unit> {
        return try {
            android.util.Log.d("AUTH_DEBUG", "Intentando registrar a: $email")
            
            // Registro en Supabase Auth
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = pass
                data = buildJsonObject {
                    put("full_name", name)
                    put("role", role)
                }
            }
            
            android.util.Log.d("AUTH_DEBUG", "Registro exitoso en Supabase para: $email")
            
            // Guardado local
            val user = UserEntity(email = email, password = pass, name = name, role = role)
            authDao.insertUser(user)
            
            Result.success(Unit)
        } catch (e: Exception) {
            android.util.Log.e("AUTH_DEBUG", "Error en registro: ${e.message}")
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<UserEntity?> {
        return authDao.getCurrentUser()
    }

    override fun getAllUsers(): Flow<List<UserEntity>> {
        return authDao.getAllUsers()
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            supabaseClient.auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
