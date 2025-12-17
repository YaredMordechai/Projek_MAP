package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.data.LaporanRepository
import kotlinx.coroutines.launch

class LaporanViewModel(
    private val repo: LaporanRepository
) : ViewModel() {

    private val _data = MutableLiveData<Any?>()
    val data: LiveData<Any?> = _data

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun load(kodePegawai: String, bulan: Int, tahun: Int) {
        viewModelScope.launch {
            val res = repo.getLaporanUser(kodePegawai, bulan, tahun)
            if (res.isSuccess) {
                _data.value = res.getOrNull()
            } else {
                _error.value = res.exceptionOrNull()?.message ?: "Gagal load laporan"
            }
        }
    }
}
