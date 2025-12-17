package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.HistoriPembayaran
import com.example.projek_map.api.Pinjaman
import com.example.projek_map.data.PinjamanRepository
import kotlinx.coroutines.launch

class PinjamanViewModel : ViewModel() {

    private val repo = PinjamanRepository()

    private val _pending = MutableLiveData<List<Pinjaman>>(emptyList())
    val pending: LiveData<List<Pinjaman>> = _pending

    private val _aktif = MutableLiveData<List<Pinjaman>>(emptyList())
    val aktif: LiveData<List<Pinjaman>> = _aktif

    private val _selesai = MutableLiveData<List<Pinjaman>>(emptyList())
    val selesai: LiveData<List<Pinjaman>> = _selesai

    // ✅ tambahan: biar Fragment bisa cek perubahan status untuk notifikasi keputusan
    private val _allPinjaman = MutableLiveData<List<Pinjaman>>(emptyList())
    val allPinjaman: LiveData<List<Pinjaman>> = _allPinjaman

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> = _toast

    private val _historiDialogText = MutableLiveData<String>()
    val historiDialogText: LiveData<String> = _historiDialogText

    fun refreshAllListsFromApi(kodePegawai: String) {
        if (kodePegawai.isBlank()) {
            _toast.value = "Kode pegawai kosong."
            return
        }

        viewModelScope.launch {
            _loading.value = true
            val res = repo.getPinjamanUser(kodePegawai)
            _loading.value = false

            if (!res.success) {
                _toast.value = res.message ?: "Gagal memuat pinjaman"
                return@launch
            }

            val list = res.data ?: emptyList()

            // ✅ set list lengkap untuk kebutuhan notifikasi keputusan
            _allPinjaman.value = list

            val p = mutableListOf<Pinjaman>()
            val a = mutableListOf<Pinjaman>()
            val s = mutableListOf<Pinjaman>()

            list.forEach { item ->
                when (item.status.lowercase()) {
                    "proses", "menunggu" -> p.add(item)
                    "disetujui", "aktif" -> a.add(item)
                    "selesai", "lunas" -> s.add(item)
                    else -> p.add(item)
                }
            }

            _pending.value = p
            _aktif.value = a
            _selesai.value = s
        }
    }

    fun ajukanPinjaman(kodePegawai: String, nominal: Int, tenor: Int) {
        if (kodePegawai.isBlank()) {
            _toast.value = "Kode pegawai kosong. Silakan login ulang."
            return
        }

        viewModelScope.launch {
            _loading.value = true
            val res = repo.createPinjaman(kodePegawai, nominal, tenor)
            _loading.value = false

            if (res.success) {
                _toast.value = "Pinjaman diajukan!"
                refreshAllListsFromApi(kodePegawai)
            } else {
                _toast.value = res.message ?: "Gagal mengajukan pinjaman"
            }
        }
    }

    fun submitPembayaranAngsuran(
        kodePegawai: String,
        pinjamanId: Int,
        jumlah: Int,
        status: String,
        buktiBase64: String?,
        buktiExt: String
    ) {
        if (kodePegawai.isBlank()) {
            _toast.value = "Kode pegawai kosong. Silakan login ulang."
            return
        }

        viewModelScope.launch {
            _loading.value = true
            val res = repo.addHistoriBukti(
                kodePegawai = kodePegawai,
                pinjamanId = pinjamanId,
                jumlah = jumlah,
                status = status,
                buktiBase64 = buktiBase64,
                buktiExt = buktiExt
            )
            _loading.value = false

            if (res.success) {
                _toast.value = "Pembayaran tercatat."
                refreshAllListsFromApi(kodePegawai)
            } else {
                _toast.value = res.message ?: "Gagal mencatat pembayaran"
            }
        }
    }

    fun loadHistoriPembayaranText(pinjamanId: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val resp = com.example.projek_map.api.ApiClient.apiService.getHistoriPembayaran(pinjamanId)
                _loading.value = false

                val body = resp.body()
                if (!resp.isSuccessful || body?.success != true) {
                    _toast.value = body?.message ?: "Gagal ambil histori"
                    return@launch
                }

                val list: List<HistoriPembayaran> = body.data ?: emptyList()

                val text = if (list.isEmpty()) {
                    "Belum ada pembayaran untuk pinjaman ini."
                } else {
                    list.joinToString("\n\n") { h ->
                        val bukti = if (h.buktiPembayaranUri.isNullOrEmpty()) "-" else "✓"
                        "Tanggal: ${h.tanggal}\nJumlah: Rp ${h.jumlah}\nStatus: ${h.status}\nBukti: $bukti"
                    }
                }

                _historiDialogText.value = text
            } catch (e: Exception) {
                _loading.value = false
                _toast.value = "Error: ${e.message}"
            }
        }
    }
}
