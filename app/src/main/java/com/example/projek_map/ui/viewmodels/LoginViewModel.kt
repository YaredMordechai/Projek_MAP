package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.Admin
import com.example.projek_map.api.AuthRepository
import com.example.projek_map.api.User
import kotlinx.coroutines.launch

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class SuccessAdmin(val admin: Admin) : LoginUiState()
    data class SuccessUser(val user: User) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val state: LiveData<LoginUiState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginUiState.Loading

            val adminResult = authRepository.loginAdmin(email, password)
            if (adminResult.success && adminResult.data != null) {
                _state.value = LoginUiState.SuccessAdmin(adminResult.data)
                return@launch
            }

            val userResult = authRepository.login(email, password)
            if (userResult.success && userResult.data != null) {
                _state.value = LoginUiState.SuccessUser(userResult.data)
                return@launch
            }

            _state.value = LoginUiState.Error(
                adminResult.message ?: userResult.message ?: "Email / password salah"
            )
        }
    }
}
