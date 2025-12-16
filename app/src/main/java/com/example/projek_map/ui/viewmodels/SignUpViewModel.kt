package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.AuthRepository
import com.example.projek_map.api.User
import kotlinx.coroutines.launch

sealed class SignupUiState {
    data object Idle : SignupUiState()
    data object Loading : SignupUiState()
    data class Success(val user: User) : SignupUiState()
    data class Error(val message: String) : SignupUiState()
}

class SignupViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableLiveData<SignupUiState>(SignupUiState.Idle)
    val state: LiveData<SignupUiState> = _state

    fun register(kodePegawai: String, email: String, password: String, nama: String) {
        viewModelScope.launch {
            _state.value = SignupUiState.Loading
            val result = authRepository.register(kodePegawai, email, password, nama)
            if (result.success && result.data != null) {
                _state.value = SignupUiState.Success(result.data)
            } else {
                _state.value = SignupUiState.Error(result.message ?: "Pendaftaran gagal")
            }
        }
    }
}
