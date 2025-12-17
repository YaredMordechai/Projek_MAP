package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.PengumumanAddRequest
import com.example.projek_map.api.PengumumanDeleteRequest
import com.example.projek_map.api.PengumumanUpdateRequest
import com.example.projek_map.api.Pengumuman
import com.example.projek_map.data.PengumumanRepository
import kotlinx.coroutines.launch

class PengumumanViewModel(
    private val repo: PengumumanRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Pengumuman>>(emptyList())
    val items: LiveData<List<Pengumuman>> = _items

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?> = _toast

    fun load() {
        viewModelScope.launch {
            val res = repo.getPengumuman()
            if (res.isSuccess) {
                _items.value = res.getOrNull() ?: emptyList()
            } else {
                _toast.value = res.exceptionOrNull()?.message ?: "Gagal memuat pengumuman"
            }
        }
    }

    fun add(req: PengumumanAddRequest) {
        viewModelScope.launch {
            val res = repo.addPengumuman(req)
            if (res.isSuccess) {
                _toast.value = "Pengumuman berhasil ditambahkan"
                load()
            } else {
                _toast.value = res.exceptionOrNull()?.message ?: "Gagal tambah pengumuman"
            }
        }
    }

    fun update(req: PengumumanUpdateRequest) {
        viewModelScope.launch {
            val res = repo.updatePengumuman(req)
            if (res.isSuccess) {
                _toast.value = "Pengumuman berhasil diupdate"
                load()
            } else {
                _toast.value = res.exceptionOrNull()?.message ?: "Gagal update pengumuman"
            }
        }
    }

    fun delete(req: PengumumanDeleteRequest) {
        viewModelScope.launch {
            val res = repo.deletePengumuman(req)
            if (res.isSuccess) {
                _toast.value = "Pengumuman berhasil dihapus"
                load()
            } else {
                _toast.value = res.exceptionOrNull()?.message ?: "Gagal hapus pengumuman"
            }
        }
    }

    fun clearToast() {
        _toast.value = null
    }
}
