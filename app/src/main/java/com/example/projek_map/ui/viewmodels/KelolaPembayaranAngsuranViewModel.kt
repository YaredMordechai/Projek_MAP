package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.AddHistoriPembayaranRequest
import com.example.projek_map.api.HistoriPembayaran
import com.example.projek_map.data.KelolaPembayaranAngsuranRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class KelolaPembayaranAngsuranViewModel : ViewModel() {

    private val repo = KelolaPembayaranAngsuranRepository()

    private val _historiList = MutableLiveData<List<HistoriPembayaran>>(emptyList())
    val historiList: LiveData<List<HistoriPembayaran>> = _historiList

    private val _toast = MutableLiveData<String?>(null)
    val toast: LiveData<String?> = _toast

    fun clearToast() {
        _toast.value = null
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val resp = repo.getHistoriPembayaranAdmin()

                if (resp.isSuccessful && resp.body()?.success == true) {
                    _historiList.value = resp.body()?.data ?: emptyList()
                } else {
                    _toast.value = resp.body()?.message ?: "Gagal ambil histori pembayaran"
                }
            } catch (e: Exception) {
                _toast.value = "Server error: ${e.message}"
            }
        }
    }

    fun decidePembayaran(historiId: Int, approve: Boolean) {
        viewModelScope.launch {
            try {
                val action = if (approve) "approve" else "reject"
                val resp = repo.decideHistoriPembayaran(
                    com.example.projek_map.api.HistoriPembayaranDecideRequest(historiId, action)
                )

                if (resp.isSuccessful && resp.body()?.success == true) {
                    _toast.value = if (approve) "Pembayaran di-ACC." else "Pembayaran ditolak."
                    refresh()
                } else {
                    _toast.value = resp.body()?.message ?: "Gagal memproses keputusan"
                }
            } catch (e: Exception) {
                _toast.value = "Server error: ${e.message}"
            }
        }
    }


    fun catatPembayaranAdmin(kodePegawai: String, pinjamanId: Int, jumlah: Int) {
        viewModelScope.launch {
            try {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

                val resp = repo.addHistoriPembayaran(
                    AddHistoriPembayaranRequest(
                        kodePegawai = kodePegawai,
                        pinjamanId = pinjamanId,
                        tanggal = today,
                        jumlah = jumlah,
                        status = "Dibayar (Admin)",
                        buktiPembayaranUri = null
                    )
                )

                if (resp.isSuccessful && resp.body()?.success == true) {
                    _toast.value = "Pembayaran dicatat."
                    refresh()
                } else {
                    _toast.value = resp.body()?.message ?: "Gagal menyimpan pembayaran"
                }
            } catch (e: Exception) {
                _toast.value = "Server error: ${e.message}"
            }
        }
    }
}
