package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.data.ProfileRepository
import com.example.projek_map.api.User
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repo: ProfileRepository
) : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _errorMsg = MutableLiveData<String?>()
    val errorMsg: LiveData<String?> = _errorMsg

    fun loadUser(kodePegawai: String) {
        if (kodePegawai.isBlank()) return
        viewModelScope.launch {
            val res = repo.getUser(kodePegawai)
            if (res.isSuccess) {
                _user.value = res.getOrNull()
            } else {
                _errorMsg.value = res.exceptionOrNull()?.message ?: "Gagal load user"
            }
        }
    }
}
