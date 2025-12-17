package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.Pinjaman
import com.example.projek_map.api.SettingsUpdateRequest
import com.example.projek_map.data.KelolaPengurusRepository
import kotlinx.coroutines.launch

class KelolaPengurusViewModel : ViewModel() {

    private val repo = KelolaPengurusRepository()

    private val _pinjamanAktif = MutableLiveData<List<Pinjaman>>(emptyList())
    val pinjamanAktif: LiveData<List<Pinjaman>> = _pinjamanAktif

    private val _pinjamanLunas = MutableLiveData<List<Pinjaman>>(emptyList())
    val pinjamanLunas: LiveData<List<Pinjaman>> = _pinjamanLunas

    // Pair(bunga, denda) supaya nggak tergantung kelas Settings di model kamu
    private val _settingsPair = MutableLiveData<Pair<Double, Double>?>(null)
    val settingsPair: LiveData<Pair<Double, Double>?> = _settingsPair

    private val _isSaving = MutableLiveData(false)
    val isSaving: LiveData<Boolean> = _isSaving

    private val _toast = MutableLiveData<String?>(null)
    val toast: LiveData<String?> = _toast

    fun refreshPinjaman() {
        viewModelScope.launch {
            try {
                val resp = repo.getAllPinjaman()
                val body = resp.body()

                if (!resp.isSuccessful || body?.success != true) {
                    _toast.value = body?.message ?: "Gagal memuat pinjaman"
                    return@launch
                }

                val all = body.data ?: emptyList()

                val aktif = mutableListOf<Pinjaman>()
                val lunas = mutableListOf<Pinjaman>()

                all.forEach { p ->
                    when (p.status.lowercase()) {
                        "disetujui", "aktif", "proses", "menunggu" -> aktif.add(p)
                        "lunas", "selesai" -> lunas.add(p)
                        else -> aktif.add(p)
                    }
                }

                _pinjamanAktif.value = aktif
                _pinjamanLunas.value = lunas

            } catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
            }
        }
    }

    fun refreshSettings() {
        viewModelScope.launch {
            try {
                val resp = repo.getSettings()
                val body = resp.body()

                if (!resp.isSuccessful || body?.success != true) {
                    // sama kayak fragment kamu: silent supaya nggak spam
                    return@launch
                }

                val s = body.data
                if (s != null) {
                    _settingsPair.value = Pair(
                        s.bungaPersen,
                        s.dendaPersenPerHari
                    )
                }
            } catch (_: Exception) {
                // silent
            }
        }
    }

    fun simpanPengaturan(bunga: Double, denda: Double) {
        viewModelScope.launch {
            try {
                _isSaving.value = true

                val resp = repo.updateSettings(
                    SettingsUpdateRequest(
                        bungaPersen = bunga,
                        dendaPersenPerHari = denda
                    )
                )
                val body = resp.body()

                _isSaving.value = false

                if (!resp.isSuccessful || body?.success != true) {
                    _toast.value = body?.message ?: "Gagal simpan pengaturan"
                    return@launch
                }

                _toast.value = "Suku bunga & denda berhasil diperbarui."
                _settingsPair.value = Pair(bunga, denda)

            } catch (e: Exception) {
                _isSaving.value = false
                _toast.value = "Error: ${e.message}"
            }
        }
    }
}
