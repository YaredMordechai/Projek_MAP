package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.*
import com.example.projek_map.api.BuktiPembayaranAnggotaRequest
import com.example.projek_map.api.SimpananTransaksiRequest
import com.example.projek_map.api.HistoriSimpanan
import com.example.projek_map.api.Simpanan
import com.example.projek_map.data.SimpananRepository
import kotlinx.coroutines.launch

class SimpananViewModel(
    private val repo: SimpananRepository
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val totalSaldo: Double = 0.0,
        val histori: List<HistoriSimpanan> = emptyList(),
        val message: String? = null,
        val popupTitle: String? = null,
        val popupMessage: String? = null
    )

    private val _state = MutableLiveData(UiState())
    val state: LiveData<UiState> = _state

    private fun setLoading(v: Boolean) {
        _state.value = _state.value?.copy(loading = v, message = null)
    }

    fun clearMessage() {
        _state.value = _state.value?.copy(message = null, popupTitle = null, popupMessage = null)
    }

    fun load(kodePegawai: String) {
        viewModelScope.launch {
            setLoading(true)
            try {
                val simpananResp = repo.getSimpanan(kodePegawai)
                val historiResp = repo.getHistoriByKodePegawai(kodePegawai)

                val simpananBody = simpananResp.body()
                val historiBody = historiResp.body()

                val simpanan: Simpanan? =
                    if (simpananResp.isSuccessful && simpananBody?.success == true) simpananBody.data else null

                val total = if (simpanan != null) {
                    simpanan.simpananPokok + simpanan.simpananWajib + simpanan.simpananSukarela
                } else 0.0

                val histori = if (historiResp.isSuccessful && historiBody?.success == true) {
                    historiBody.data.orEmpty()
                } else emptyList()



                _state.value = _state.value?.copy(
                    loading = false,
                    totalSaldo = total,
                    histori = histori,
                    message = null
                )
            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    loading = false,
                    totalSaldo = 0.0,
                    histori = emptyList(),
                    message = e.message ?: "Gagal ambil data simpanan"
                )
            }
        }
    }

    fun setor(kodePegawai: String, jenis: String, jumlah: Double) {
        transaksiInternal(kodePegawai, jenis, jumlah, isSetor = true)
    }

    fun tarik(kodePegawai: String, jenis: String, jumlah: Double) {
        transaksiInternal(kodePegawai, jenis, jumlah, isSetor = false)
    }

    private fun transaksiInternal(kodePegawai: String, jenis: String, jumlah: Double, isSetor: Boolean) {
        viewModelScope.launch {
            setLoading(true)
            try {
                val req = SimpananTransaksiRequest(
                    kodePegawai = kodePegawai,
                    jenisInput = jenis,
                    jumlah = if (isSetor) jumlah else -jumlah,
                    keterangan = "-"
                )
                val resp = repo.transaksi(req)
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true) {
                    // reload
                    load(kodePegawai)

                    val formatted = String.format("%,.0f", jumlah).replace(',', '.')
                    _state.value = _state.value?.copy(
                        popupTitle = if (isSetor) "Setoran Berhasil" else "Penarikan Berhasil",
                        popupMessage = (if (isSetor) "Setoran" else "Penarikan") +
                                " $jenis sebesar Rp $formatted tercatat."
                    )
                } else {
                    _state.value = _state.value?.copy(
                        loading = false,
                        message = body?.message ?: (if (isSetor) "Gagal setor simpanan" else "Gagal tarik simpanan")
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    loading = false,
                    message = e.message ?: "Gagal konek ke server"
                )
            }
        }
    }

    fun uploadBukti(kodePegawai: String, uri: String) {
        viewModelScope.launch {
            setLoading(true)
            try {
                val resp = repo.uploadBukti(BuktiPembayaranAnggotaRequest(kodePegawai = kodePegawai, uri = uri))
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true) {
                    _state.value = _state.value?.copy(
                        loading = false,
                        popupTitle = "Bukti Pembayaran",
                        popupMessage = "Bukti berhasil dikirim. Status: menunggu verifikasi admin."
                    )
                } else {
                    _state.value = _state.value?.copy(
                        loading = false,
                        popupTitle = "Bukti Pembayaran",
                        popupMessage = body?.message ?: "Gagal kirim bukti ke server"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    loading = false,
                    popupTitle = "Bukti Pembayaran",
                    popupMessage = e.message ?: "Gagal konek ke server"
                )
            }
        }
    }

    class Factory(
        private val repo: SimpananRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SimpananViewModel(repo) as T
        }
    }
}
