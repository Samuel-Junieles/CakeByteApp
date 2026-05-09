package com.example.cakebyteapp.data.repository

import com.example.cakebyteapp.data.local.dao.AuthDao
import com.example.cakebyteapp.data.local.entity.UserEntity
import com.example.cakebyteapp.data.remote.dto.ProfileDto
import com.example.cakebyteapp.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDao: AuthDao,
    private val supabaseClient: SupabaseClient,
) : AuthRepository {
    
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    override suspend fun login(email: String, pass: String): Result<UserEntity> {
        return try {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = pass
            }
            
            val metadata = supabaseClient.auth.currentUserOrNull()?.userMetadata
            val name = metadata?.get("full_name")?.toString()?.replace("\"", "") ?: "Usuario"
            val role = metadata?.get("role")?.toString()?.replace("\"", "") ?: "Comprador"
            
            val existingUser = authDao.getUserByEmail(email)
            val user = existingUser?.copy(name = name, role = role, password = pass) 
                      ?: UserEntity(email = email, password = pass, name = name, role = role)
            
            authDao.insertUser(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, pass: String, name: String, role: String): Result<Unit> {
        return try {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = pass
                data = buildJsonObject {
                    put("full_name", name)
                    put("role", role)
                }
            }
            
            try {
                val userId = supabaseClient.auth.currentUserOrNull()?.id
                if (userId != null) {
                    supabaseClient.postgrest["profiles"].insert(
                        buildJsonObject {
                            put("id", userId)
                            put("email", email)
                            put("name", name)
                            put("role", role)
                        }
                    )
                }
            } catch (ex: Exception) {
                android.util.Log.e("AUTH_DEBUG", "Error Supabase profiles: ${ex.message}")
            }
            
            authDao.insertUser(UserEntity(email = email, password = pass, name = name, role = role))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<UserEntity?> = authDao.getCurrentUser()

    override fun getAllUsers(): Flow<List<UserEntity>> {
        return authDao.getAllUsers().onStart {
            repositoryScope.launch {
                try {
                    val supabaseProfiles = supabaseClient.postgrest["profiles"]
                        .select().decodeList<ProfileDto>()
                    
                    supabaseProfiles.forEach { profile ->
                        val local = authDao.getUserByEmail(profile.email)
                        val userEntity = UserEntity(
                            id = local?.id ?: 0,
                            email = profile.email,
                            name = profile.name,
                            role = profile.role,
                            address = profile.address,
                            phone = profile.phone
                        )
                        authDao.insertUser(userEntity)
                    }
                } catch (e: Exception) {
                    android.util.Log.e("SYNC_DEBUG", "Error sync Supabase: ${e.message}")
                }
            }
        }
    }

    override suspend fun getUserByEmail(email: String): UserEntity? = authDao.getUserByEmail(email)

    override suspend fun updateUser(user: UserEntity): Result<Unit> {
        return try {
            authDao.insertUser(user)
            repositoryScope.launch {
                try {
                    supabaseClient.postgrest["profiles"].update(
                        buildJsonObject {
                            put("name", user.name)
                            put("address", user.address)
                            put("phone", user.phone)
                        }
                    ) {
                        filter { eq("email", user.email) }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("SYNC_DEBUG", "Error updating Supabase: ${e.message}")
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            supabaseClient.auth.signOut()
            authDao.clearAllUsers()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
