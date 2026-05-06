package com.example.cakebyteapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cakebyteapp.data.local.entity.UserEntity
import com.example.cakebyteapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedRole = MutableStateFlow("Todos")

    // Ejemplo de flujo de datos combinando búsqueda y filtros
    // En un entorno real, esto vendría del AuthRepository que observa la DB
    val users: StateFlow<List<UserEntity>> = combine(_searchQuery, _selectedRole) { query, role ->
        // Simulación de filtrado (en real sería una query de Room)
        emptyList<UserEntity>() // Aquí iría la lógica de filtrado
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onRoleFilterChanged(role: String) {
        _selectedRole.value = role
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            // Lógica para borrar usuario vía UseCase/Repository
        }
    }
}