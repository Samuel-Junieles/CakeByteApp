package com.example.cakebyteapp.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cakebyteapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _logoutSuccess = MutableSharedFlow<Boolean>()
    val logoutSuccess = _logoutSuccess.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            val result = authRepository.logout()
            _logoutSuccess.emit(result.isSuccess)
        }
    }
}