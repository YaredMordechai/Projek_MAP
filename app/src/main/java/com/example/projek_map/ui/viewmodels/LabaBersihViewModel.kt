package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.data.LabaRepository
import kotlinx.coroutines.launch

data class LabaBersihUi(
    val totalMasuk: Double,
    val totalKeluar: Double,
    val labaBersih: Double
)

class LabaBersihViewModel : ViewModel() {

    private val repo = LabaRepository()

    private val _labaBersihUi = MutableLiveData<LabaBersihUi?>(null)
    val labaBersihUi: LiveData<LabaBersihUi?> = _labaBersihUi

    fun loadLabaBersih() {
        viewModelScope.launch {
            try {
                val resp = repo.getLabaBersih()
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    val d = body.data
                    _labaBersihUi.value = LabaBersihUi(
                        totalMasuk = d.totalMasuk,
                        totalKeluar = d.totalKeluar,
                        labaBersih = d.labaBersih
                    )
                } else {
                    _labaBersihUi.value = null
                }
            } catch (_: Exception) {
                _labaBersihUi.value = null
            }
        }
    }
}
