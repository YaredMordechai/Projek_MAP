package com.example.projek_map.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projek_map.data.DashboardRepository
import com.example.projek_map.data.DashboardTotals
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    data object Idle : DashboardUiState()
    data object Loading : DashboardUiState()
    data class Success(val totals: DashboardTotals) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(
    private val repo: DashboardRepository = DashboardRepository()
) : ViewModel() {

    private val _state = MutableLiveData<DashboardUiState>(DashboardUiState.Idle)
    val state: LiveData<DashboardUiState> = _state

    fun loadUserTotals(kodePegawai: String) {
        if (kodePegawai.isBlank()) {
            _state.value = DashboardUiState.Error("Kode pegawai kosong.")
            return
        }
        viewModelScope.launch {
            _state.value = DashboardUiState.Loading
            val res = repo.getUserTotals(kodePegawai)
            _state.value = res.fold(
                onSuccess = { DashboardUiState.Success(it) },
                onFailure = { DashboardUiState.Error(it.message ?: "Gagal memuat data") }
            )
        }
    }
}
