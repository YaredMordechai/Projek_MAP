package com.example.projek_map.data

import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.KoperasiApiService
import com.example.projek_map.api.SettingsUpdateRequest

class KelolaPengurusRepository(
    private val api: KoperasiApiService = ApiClient.apiService
) {
    suspend fun getAllPinjaman() = api.getAllPinjaman()
    suspend fun getSettings() = api.getSettings()
    suspend fun updateSettings(req: SettingsUpdateRequest) = api.updateSettings(req)
}
