package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.AddUserRequest
import com.example.projek_map.api.DeleteUserRequest
import com.example.projek_map.api.SetUserStatusRequest
import com.example.projek_map.api.UpdateUserRequest
import com.example.projek_map.api.User
import com.example.projek_map.data.AnggotaRepository
import com.example.projek_map.data.RepoResult
import com.example.projek_map.utils.Event
import kotlinx.coroutines.launch

enum class AnggotaAction {
    LOAD, ADD, UPDATE, SET_STATUS, DELETE
}

data class AnggotaActionResult(
    val action: AnggotaAction,
    val success: Boolean,
    val message: String
)

class KelolaAnggotaViewModel(
    private val repo: AnggotaRepository
) : ViewModel() {

    private val _anggotaList = MutableLiveData<List<User>>(emptyList())
    val anggotaList: LiveData<List<User>> = _anggotaList

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _actionResult = MutableLiveData<Event<AnggotaActionResult>>()
    val actionResult: LiveData<Event<AnggotaActionResult>> = _actionResult

    fun loadAnggota() {
        viewModelScope.launch {
            _loading.value = true
            when (val result = repo.getAllUsers()) {
                is RepoResult.Success -> {
                    _anggotaList.value = result.data
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.LOAD,
                            success = true,
                            message = result.message ?: "Berhasil memuat anggota"
                        )
                    )
                }
                is RepoResult.Error -> {
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.LOAD,
                            success = false,
                            message = result.message
                        )
                    )
                }
            }
            _loading.value = false
        }
    }

    fun addUser(req: AddUserRequest) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.addUser(req)
            _loading.value = false

            when (result) {
                is RepoResult.Success -> {
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.ADD,
                            success = true,
                            message = result.message ?: "Anggota ditambahkan"
                        )
                    )
                    loadAnggota()
                }
                is RepoResult.Error -> {
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.ADD,
                            success = false,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun updateUser(req: UpdateUserRequest) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.updateUser(req)
            _loading.value = false

            when (result) {
                is RepoResult.Success -> {
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.UPDATE,
                            success = true,
                            message = result.message ?: "Data diperbarui"
                        )
                    )
                    loadAnggota()
                }
                is RepoResult.Error -> {
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.UPDATE,
                            success = false,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun setStatusUser(req: SetUserStatusRequest) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.setUserStatus(req)
            _loading.value = false

            when (result) {
                is RepoResult.Success -> {
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.SET_STATUS,
                            success = true,
                            message = result.message ?: "Status diubah"
                        )
                    )
                    loadAnggota()
                }
                is RepoResult.Error -> {
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.SET_STATUS,
                            success = false,
                            message = result.message
                        )
                    )
                }
            }
        }
    }

    fun deleteUser(req: DeleteUserRequest) {
        viewModelScope.launch {
            _loading.value = true
            val result = repo.deleteUser(req)
            _loading.value = false

            when (result) {
                is RepoResult.Success -> {
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.DELETE,
                            success = true,
                            message = result.message ?: "Anggota dihapus"
                        )
                    )
                    loadAnggota()
                }
                is RepoResult.Error -> {
                    _actionResult.value = Event(
                        AnggotaActionResult(
                            action = AnggotaAction.DELETE,
                            success = false,
                            message = result.message
                        )
                    )
                }
            }
        }
    }
}
