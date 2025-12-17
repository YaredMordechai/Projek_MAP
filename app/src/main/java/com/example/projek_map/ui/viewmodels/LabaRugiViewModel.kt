package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.data.LabaRepository
import kotlinx.coroutines.launch

data class LabaRugiUi(
    val totalPendapatan: Double,
    val totalBeban: Double,
    val labaRugi: Double,
    val isLaba: Boolean
)

class LabaRugiViewModel : ViewModel() {

    private val repo = LabaRepository()

    private val _labaRugiUi = MutableLiveData<LabaRugiUi?>(null)
    val labaRugiUi: LiveData<LabaRugiUi?> = _labaRugiUi

    private val _toast = MutableLiveData<String?>(null)
    val toast: LiveData<String?> = _toast

    fun clearToast() { _toast.value = null }

    fun loadFromDb(year: Int, bulan: Int) {
        viewModelScope.launch {
            try {
                val resp = repo.getLabaRugi(year, bulan)
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    val d = body.data
                    _labaRugiUi.value = LabaRugiUi(
                        totalPendapatan = d.totalPendapatan,
                        totalBeban = d.totalBeban,
                        labaRugi = d.labaRugi,
                        isLaba = d.isLaba
                    )
                } else {
                    _toast.value = "Gagal load laba rugi dari server"
                }
            } catch (e: Exception) {
                _toast.value = "Error: ${e.message}"
            }
        }
    }
}
