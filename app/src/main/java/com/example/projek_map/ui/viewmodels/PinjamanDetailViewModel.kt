package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.AngsuranItem
import com.example.projek_map.api.RincianPinjaman
import com.example.projek_map.data.PinjamanRepository
import kotlinx.coroutines.launch

class PinjamanDetailViewModel : ViewModel() {

    private val repo = PinjamanRepository()

    private val _rincian = MutableLiveData<RincianPinjaman?>()
    val rincian: LiveData<RincianPinjaman?> = _rincian

    private val _jadwal = MutableLiveData<List<AngsuranItem>>(emptyList())
    val jadwal: LiveData<List<AngsuranItem>> = _jadwal

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    fun load(pinjamanId: Int, metode: String) {
        viewModelScope.launch {
            try {
                _loading.value = true

                // ✅ FIX: repo return ApiResponse<T>
                val rincResp = repo.getRincianPinjamanDb(pinjamanId, metode)
                val jadResp = repo.getJadwalPinjamanDb(pinjamanId, metode)

                _loading.value = false

                if (rincResp.success) _rincian.value = rincResp.data
                else _toast.value = rincResp.message ?: "Gagal memuat rincian pinjaman"

                if (jadResp.success) _jadwal.value = jadResp.data ?: emptyList()
                else _toast.value = jadResp.message ?: "Gagal memuat jadwal angsuran"

            } catch (e: Exception) {
                _loading.value = false
                _toast.value = e.message ?: "Gagal memuat detail pinjaman"
            }
        }
    }

    fun bayar(
        kodePegawai: String,
        pinjamanId: Int,
        jumlah: Int,
        currentMetode: String
    ) {
        if (kodePegawai.isBlank()) {
            _toast.value = "Kode pegawai kosong. Silakan login ulang."
            return
        }

        viewModelScope.launch {
            _loading.value = true

            // ✅ FIX: sekarang function ini ada di PinjamanRepository (lihat bawah)
            val res = repo.bayarAngsuranDb(kodePegawai, pinjamanId, jumlah)

            _loading.value = false

            if (res.success) {
                _toast.value = "Pembayaran tercatat."
                load(pinjamanId, currentMetode)
            } else {
                _toast.value = res.message ?: "Gagal mencatat pembayaran"
            }
        }
    }
}
