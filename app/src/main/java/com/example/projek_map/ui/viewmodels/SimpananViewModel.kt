package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.*
import com.example.projek_map.api.HistoriSimpanan
import com.example.projek_map.api.Simpanan
import com.example.projek_map.api.SimpananTransaksiRequest
import com.example.projek_map.data.SimpananRepository
import kotlinx.coroutines.launch

data class SimpananUiState(
    val totalSaldo: Double = 0.0,
    val histori: List<HistoriSimpanan> = emptyList(),

    val message: String? = null,
    val popupTitle: String? = null,
    val popupMessage: String? = null,

    val buktiBase64: String? = null,
    val buktiExt: String = "jpg",

    val resetForm: Boolean = false // ✅ TAMBAH INI
)


class SimpananViewModel(
    private val repo: SimpananRepository
) : ViewModel() {

    private val _state = MutableLiveData(SimpananUiState())
    val state: LiveData<SimpananUiState> = _state

    // =========================
    // LOAD DATA (saldo + histori)
    // =========================
    fun load(kodePegawai: String) {
        viewModelScope.launch {
            try {
                val saldoResp = repo.getSimpanan(kodePegawai)
                val saldoBody = saldoResp.body()

                val simpanan: Simpanan? = if (saldoResp.isSuccessful && saldoBody?.success == true) {
                    saldoBody.data
                } else null

                val totalSaldo = (simpanan?.simpananPokok ?: 0.0) +
                        (simpanan?.simpananWajib ?: 0.0) +
                        (simpanan?.simpananSukarela ?: 0.0)

                val histResp = repo.getHistoriByKodePegawai(kodePegawai)
                val histBody = histResp.body()

                val histori = if (histResp.isSuccessful && histBody?.success == true) {
                    histBody.data ?: emptyList()
                } else emptyList()

                _state.value = _state.value!!.copy(
                    totalSaldo = totalSaldo,
                    histori = histori
                )
            } catch (e: Exception) {
                _state.value = _state.value!!.copy(
                    message = "Gagal load simpanan: ${e.message}"
                )
            }
        }
    }

    fun clearResetFlag() {
        val st = _state.value ?: return
        _state.value = st.copy(resetForm = false)
    }

    // =========================
    // SET BUKTI (dipanggil dari Fragment)
    // =========================
    fun uploadBukti(kodePegawai: String, uri: String) {
        // NOTE:
        // Fragment kamu saat ini kirim uri "content://..." ke endpoint bukti_pembayaran_anggota_add,
        // itu tidak berguna untuk server.
        // Jadi method ini kita pakai untuk "ingat bahwa user sudah memilih bukti"
        // (base64 nya sebaiknya dikirim lewat setor()).
        _state.value = _state.value!!.copy(
            message = "Bukti dipilih. Silakan klik Setor untuk mengirim.",
        )
    }

    // dipanggil Fragment setelah convert uri → base64
    fun setBuktiBase64(base64: String, ext: String = "jpg") {
        _state.value = _state.value!!.copy(
            buktiBase64 = base64,
            buktiExt = ext.ifBlank { "jpg" }
        )
    }

    // =========================
    // SETOR (wajib buktiBase64)
    // =========================
    fun setor(kodePegawai: String, jenis: String, jumlah: Double) {
        viewModelScope.launch {
            try {
                val st = _state.value ?: SimpananUiState()

                if (jumlah <= 0) {
                    _state.value = st.copy(message = "Jumlah setor tidak valid")
                    return@launch
                }

                if (st.buktiBase64.isNullOrBlank()) {
                    _state.value = st.copy(message = "Upload/ambil foto bukti dulu")
                    return@launch
                }

                val req = SimpananTransaksiRequest(
                    kodePegawai = kodePegawai,
                    jenisInput = jenis,
                    jumlah = jumlah,
                    keterangan = "-",
                    buktiBase64 = st.buktiBase64,
                    buktiExt = st.buktiExt
                )

                val resp = repo.transaksi(req)
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true) {
                    _state.value = st.copy(
                        message = body.message ?: "Setoran dikirim. Menunggu verifikasi admin.",
                        // reset bukti setelah sukses kirim
                        buktiBase64 = null,
                        buktiExt = "jpg",
                        resetForm = true
                    )
                    // reload saldo+histori (saldo belum berubah sampai admin approve, tapi histori/pesan bisa update)
                    load(kodePegawai)
                } else {
                    _state.value = st.copy(message = body?.message ?: "Gagal setor simpanan")
                }
            } catch (e: Exception) {
                val st = _state.value ?: SimpananUiState()
                _state.value = st.copy(message = "Error setor: ${e.message}")
            }
        }
    }

    // =========================
    // TARIK (tanpa bukti, jumlah harus negatif di server)
    // =========================
    fun tarik(kodePegawai: String, jenis: String, jumlah: Double) {
        viewModelScope.launch {
            try {
                val st = _state.value ?: SimpananUiState()

                if (jumlah <= 0) {
                    _state.value = st.copy(message = "Jumlah tarik tidak valid")
                    return@launch
                }

                val req = SimpananTransaksiRequest(
                    kodePegawai = kodePegawai,
                    jenisInput = jenis,
                    jumlah = -kotlin.math.abs(jumlah),
                    keterangan = "-"
                )

                val resp = repo.transaksi(req)
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true) {
                    _state.value = st.copy(message = body.message ?: "Penarikan berhasil")
                    load(kodePegawai)
                } else {
                    _state.value = st.copy(message = body?.message ?: "Gagal tarik simpanan")
                }
            } catch (e: Exception) {
                val st = _state.value ?: SimpananUiState()
                _state.value = st.copy(message = "Error tarik: ${e.message}")
            }
        }
    }

    fun clearMessage() {
        val st = _state.value ?: return
        _state.value = st.copy(message = null, popupTitle = null, popupMessage = null)
    }

    // =========================
    // FACTORY (biar Fragment kamu bisa: by viewModels { Factory(...) }
    // =========================
    class Factory(private val repo: SimpananRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SimpananViewModel::class.java)) {
                return SimpananViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
