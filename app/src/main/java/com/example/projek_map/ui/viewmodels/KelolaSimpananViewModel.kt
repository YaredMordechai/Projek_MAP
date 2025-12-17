package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.*
import com.example.projek_map.api.SimpananPending
import com.example.projek_map.api.SimpananPendingDecideRequest
import com.example.projek_map.api.HistoriSimpanan
import com.example.projek_map.api.Simpanan
import com.example.projek_map.data.SimpananRepository
import com.example.projek_map.api.TransaksiSimpanan
import kotlinx.coroutines.launch
import kotlin.math.abs

class KelolaSimpananViewModel(
    private val repo: SimpananRepository
) : ViewModel() {

    data class HeaderState(
        val totalPokok: Double = 0.0,
        val totalWajib: Double = 0.0,
        val totalSukarela: Double = 0.0,
        val kodePegawaiOptions: List<String> = emptyList()
    )

    data class UiState(
        val loading: Boolean = false,
        val message: String? = null,
        val header: HeaderState = HeaderState(),
        val list: List<TransaksiSimpanan> = emptyList(),
        val pendingMap: Map<Int, SimpananPending> = emptyMap()
    )

    private val _state = MutableLiveData(UiState())
    val state: LiveData<UiState> = _state

    fun loadAll() {
        viewModelScope.launch {
            _state.value = _state.value?.copy(loading = true, message = null)

            try {
                // 1) header totals
                val simpananResp = repo.getAllSimpanan()
                val simpananBody = simpananResp.body()

                val simpananList: List<Simpanan> =
                    if (simpananResp.isSuccessful && simpananBody?.success == true) simpananBody.data.orEmpty()
                    else emptyList()

                val kodeOptions = simpananList.map { it.kodePegawai }.distinct().sorted()

                val header = HeaderState(
                    totalPokok = simpananList.sumOf { it.simpananPokok },
                    totalWajib = simpananList.sumOf { it.simpananWajib },
                    totalSukarela = simpananList.sumOf { it.simpananSukarela },
                    kodePegawaiOptions = kodeOptions
                )

                // 2) histori list
                val historiResp = repo.getAllHistori()
                val historiBody = historiResp.body()
                val historiList: List<HistoriSimpanan> =
                    if (historiResp.isSuccessful && historiBody?.success == true) historiBody.data.orEmpty()
                    else emptyList()

                val mappedHistori = historiList.map { h ->
                    TransaksiSimpanan(
                        id = h.id,
                        kodePegawai = h.kodePegawai,
                        jenis = extractJenis(h.jenis),
                        jumlah = abs(h.jumlah),
                        tanggal = h.tanggal ?: ""
                    )
                }

                // 3) pending
                val pendingResp = repo.getPending()
                val pendingBody = pendingResp.body()
                val pendingList: List<SimpananPending> =
                    if (pendingResp.isSuccessful && pendingBody?.success == true) pendingBody.data.orEmpty()
                    else emptyList()

                val pendingMap = pendingList.associateBy { it.id }

                val mappedPending = pendingList.map { p ->
                    TransaksiSimpanan(
                        id = -p.id, // negatif = pending
                        kodePegawai = p.kodePegawai,
                        jenis = extractJenis(p.jenisInput) + " (Pending)",
                        jumlah = abs(p.jumlah),
                        tanggal = p.tanggal ?: ""
                    )
                }

                _state.value = _state.value?.copy(
                    loading = false,
                    header = header,
                    list = mappedPending + mappedHistori, // pending di atas
                    pendingMap = pendingMap,
                    message = null
                )

            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    loading = false,
                    message = e.message ?: "Error load data"
                )
            }
        }
    }

    fun decidePending(pendingId: Int, action: String) {
        viewModelScope.launch {
            try {
                val resp = repo.decidePending(SimpananPendingDecideRequest(id = pendingId, action = action))
                val body = resp.body()
                if (resp.isSuccessful && body?.success == true) {
                    loadAll()
                    _state.value = _state.value?.copy(message = "Berhasil: $action")
                } else {
                    _state.value = _state.value?.copy(message = body?.message ?: "Gagal: $action")
                }
            } catch (e: Exception) {
                _state.value = _state.value?.copy(message = e.message ?: "Error: decide pending")
            }
        }
    }

    private fun extractJenis(jenisHistori: String): String {
        val s = jenisHistori.lowercase()
        return when {
            "pokok" in s -> "Pokok"
            "wajib" in s -> "Wajib"
            else -> "Sukarela"
        }
    }

    class Factory(
        private val repo: SimpananRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return KelolaSimpananViewModel(repo) as T
        }
    }
}
