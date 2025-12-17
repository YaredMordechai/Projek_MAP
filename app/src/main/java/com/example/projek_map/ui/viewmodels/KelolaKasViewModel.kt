package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.KasDeleteRequest
import com.example.projek_map.api.KasTransaksi
import com.example.projek_map.api.KasTransaksiAddRequest
import com.example.projek_map.api.KasTransaksiUpdateRequest
import com.example.projek_map.data.KasRepository
import com.example.projek_map.utils.Event
import kotlinx.coroutines.launch

class KelolaKasViewModel : ViewModel() {

    private val repo = KasRepository()

    private val _kasList = MutableLiveData<List<KasTransaksi>>(emptyList())
    val kasList: LiveData<List<KasTransaksi>> = _kasList

    private val _toastEvent = MutableLiveData<Event<String>>()
    val toastEvent: LiveData<Event<String>> = _toastEvent

    fun loadKas() {
        viewModelScope.launch {
            try {
                val res = repo.getKas()
                if (res.isSuccessful && res.body()?.success == true) {
                    _kasList.value = res.body()?.data ?: emptyList()
                } else {
                    _toastEvent.value = Event(res.body()?.message ?: "Gagal memuat kas")
                }
            } catch (e: Exception) {
                _toastEvent.value = Event("Error: ${e.message}")
            }
        }
    }

    fun addKas(req: KasTransaksiAddRequest) {
        viewModelScope.launch {
            try {
                val res = repo.addKas(req)
                if (res.isSuccessful && res.body()?.success == true) {
                    _toastEvent.value = Event("Transaksi kas ditambahkan.")
                    loadKas()
                } else {
                    _toastEvent.value = Event(res.body()?.message ?: "Gagal tambah kas")
                }
            } catch (e: Exception) {
                _toastEvent.value = Event("Error: ${e.message}")
            }
        }
    }

    fun updateKas(req: KasTransaksiUpdateRequest) {
        viewModelScope.launch {
            try {
                val res = repo.updateKas(req)
                if (res.isSuccessful && res.body()?.success == true) {
                    _toastEvent.value = Event("Transaksi kas diperbarui.")
                    loadKas()
                } else {
                    _toastEvent.value = Event(res.body()?.message ?: "Gagal update kas")
                }
            } catch (e: Exception) {
                _toastEvent.value = Event("Error: ${e.message}")
            }
        }
    }

    fun deleteKas(req: KasDeleteRequest) {
        viewModelScope.launch {
            try {
                val res = repo.deleteKas(req)
                if (res.isSuccessful && res.body()?.success == true) {
                    _toastEvent.value = Event("Transaksi kas dihapus.")
                    loadKas()
                } else {
                    _toastEvent.value = Event(res.body()?.message ?: "Gagal hapus")
                }
            } catch (e: Exception) {
                _toastEvent.value = Event("Error: ${e.message}")
            }
        }
    }
}
