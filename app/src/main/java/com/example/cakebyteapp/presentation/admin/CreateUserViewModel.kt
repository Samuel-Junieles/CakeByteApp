package com.example.cakebyteapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cakebyteapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateUserUiState>(CreateUserUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<CreateUserNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun saveUser(name: String, surname: String, email: String, phone: String, role: String) {
        if (name.isBlank() || email.isBlank()) {
            _uiState.value = CreateUserUiState.Error("Nombre y Correo son obligatorios")
            return
        }

        viewModelScope.launch {
            _uiState.value = CreateUserUiState.Loading
            // Simulación de guardado
            val result = authRepository.register(email, "password123", "$name $surname", role)
            if (result.isSuccess) {
                _uiState.value = CreateUserUiState.Success
                _navigationEvent.emit(CreateUserNavigationEvent.NavigateBack)
            } else {
                _uiState.value = CreateUserUiState.Error(result.exceptionOrNull()?.message ?: "Error al guardar")
            }
        }
    }

    sealed class CreateUserUiState {
        object Idle : CreateUserUiState()
        object Loading : CreateUserUiState()
        object Success : CreateUserUiState()
        data class Error(val message: String) : CreateUserUiState()
    }

    sealed class CreateUserNavigationEvent {
        object NavigateBack : CreateUserNavigationEvent()
    }
}