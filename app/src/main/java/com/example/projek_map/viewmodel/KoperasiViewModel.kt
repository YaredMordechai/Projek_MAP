package com.example.projek_map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.projek_map.data.Admin
import com.example.projek_map.data.KoperasiRepository
import com.example.projek_map.data.User
import kotlinx.coroutines.launch

data class LoginState(
    val user: User? = null,
    val admin: Admin? = null
)

class KoperasiViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = KoperasiRepository(application)

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> = _loginState

    private val _registerResult = MutableLiveData<User?>()
    val registerResult: LiveData<User?> = _registerResult

    private val _registerError = MutableLiveData<String?>()
    val registerError: LiveData<String?> = _registerError

    init {
        viewModelScope.launch {
            repo.prepopulateIfNeeded()
            loadUsers()
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = repo.getAllUsers()
        }
    }

    fun tambahAnggota(nama: String, email: String) {
        viewModelScope.launch {
            repo.tambahAnggota(nama, email)
            loadUsers()
        }
    }

    fun updateAnggota(kodePegawai: String, namaBaru: String, emailBaru: String) {
        viewModelScope.launch {
            repo.updateAnggota(kodePegawai, namaBaru, emailBaru)
            loadUsers()
        }
    }

    fun nonaktifkanAnggota(user: User) {
        viewModelScope.launch {
            repo.nonaktifkanAnggota(user)
            loadUsers()
        }
    }

    fun hapusAnggota(user: User) {
        viewModelScope.launch {
            repo.hapusAnggota(user)
            loadUsers()
        }
    }

    // ======================
    // LOGIN (email ATAU kode)
    // ======================
    fun login(identifier: String, password: String) {
        viewModelScope.launch {
            // Coba admin dulu
            val admin = repo.loginAdminByIdentifier(identifier, password)
            val user = if (admin == null) {
                repo.loginUserByIdentifier(identifier, password)
            } else null

            _loginState.value = LoginState(user = user, admin = admin)
        }
    }

    // ======================
    // SIGNUP
    // ======================
    fun register(kodePegawai: String, email: String, password: String, nama: String) {
        viewModelScope.launch {
            if (repo.isUserExist(kodePegawai, email)) {
                _registerError.value = "User sudah terdaftar!"
                _registerResult.value = null
            } else {
                val user = repo.registerUser(kodePegawai, email, password, nama)
                _registerError.value = null
                _registerResult.value = user
                loadUsers()
            }
        }
    }
}
