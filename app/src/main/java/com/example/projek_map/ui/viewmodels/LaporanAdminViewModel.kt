package com.example.projek_map.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.User
import kotlinx.coroutines.launch

class LaporanAdminViewModel : ViewModel() {

    data class Summary(
        val saldoKas: Double,
        val totalSimpananAll: Double,
        val totalPinjamanAll: Double,
        val jumlahAnggota: Int
    )

    private val _summary = MutableLiveData<Summary?>()
    val summary: LiveData<Summary?> = _summary

    private val _filteredList = MutableLiveData<List<LaporanAdminFragment.UserSummary>>(emptyList())
    val filteredList: LiveData<List<LaporanAdminFragment.UserSummary>> = _filteredList

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    // cache data dari API (supaya filter tidak hit API terus) -> pindah ke VM
    private val anggotaCache = mutableListOf<LaporanAdminFragment.UserSummary>()

    private var currentQuery: String = ""
    private var currentStatus: String = "Semua"

    fun loadAll() {
        if (_isLoading.value == true) return
        _isLoading.value = true
        _errorMessage.value = null
        _summary.value = null
        _filteredList.value = emptyList()
        anggotaCache.clear()

        viewModelScope.launch {
            try {
                val resp = ApiClient.apiService.getLaporanAdmin()
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    val d = body.data

                    _summary.value = Summary(
                        saldoKas = d.saldoKas,
                        totalSimpananAll = d.totalSimpananAll,
                        totalPinjamanAll = d.totalPinjamanAll,
                        jumlahAnggota = d.jumlahAnggota
                    )

                    anggotaCache.addAll(
                        d.users.map { r ->
                            LaporanAdminFragment.UserSummary(
                                user = User(
                                    kodePegawai = r.kodePegawai,
                                    email = r.email,
                                    password = "", // server sengaja kosong
                                    nama = r.nama,
                                    statusKeanggotaan = normalizeStatus(r.statusKeanggotaan)
                                ),
                                totalSimpanan = r.totalSimpanan,
                                totalPinjamanAktif = r.totalPinjamanAktif
                            )
                        }
                    )

                    applyFilterFromCache()
                } else {
                    _errorMessage.value =
                        body?.message?.takeIf { it.isNotBlank() }
                            ?: "Gagal memuat data laporan admin (response tidak valid)."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal terhubung ke server: ${e.message ?: "Unknown error"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setFilter(query: String, status: String) {
        currentQuery = query.trim()
        currentStatus = status.trim().ifBlank { "Semua" }
        applyFilterFromCache()
    }

    private fun applyFilterFromCache() {
        val q = currentQuery.lowercase()
        val status = currentStatus

        val list = anggotaCache
            .filter { row ->
                val user = row.user

                val okStatus = when (status) {
                    "Aktif" -> user.statusKeanggotaan.equals("Aktif", true)
                    "Nonaktif" -> user.statusKeanggotaan.equals("Nonaktif", true)
                    else -> true
                }
                if (!okStatus) return@filter false

                if (q.isBlank()) return@filter true
                "${user.nama} ${user.kodePegawai} ${user.email}".lowercase().contains(q)
            }
            .sortedBy { it.user.nama.lowercase() }

        _filteredList.value = list
    }

    // Normalisasi semua variasi jadi cuma: "Aktif" / "Nonaktif"
    private fun normalizeStatus(raw: String?): String {
        val s = raw?.trim().orEmpty().lowercase()

        return when {
            // nonaktif variants
            s.contains("nonaktif") -> "Nonaktif"
            s.contains("non-aktif") -> "Nonaktif"
            s.contains("tidak aktif") -> "Nonaktif"
            s.contains("inactive") -> "Nonaktif"
            s == "0" -> "Nonaktif"

            // aktif variants (pastikan tidak ketabrak "tidak aktif")
            s.contains("aktif") -> "Aktif"
            s.contains("active") -> "Aktif"
            s == "1" -> "Aktif"

            else -> if (s.isBlank()) "Nonaktif" else raw!!.trim()
        }
    }
}
