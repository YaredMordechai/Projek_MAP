package com.example.projek_map.data

import com.example.projek_map.api.AddHistoriPembayaranRequest
import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.HistoriPembayaranDecideRequest
import com.example.projek_map.api.KoperasiApiService

class KelolaPembayaranAngsuranRepository(
    private val api: KoperasiApiService = ApiClient.apiService
) {
    suspend fun getHistoriPembayaranAdmin() = api.getHistoriPembayaranAdmin()
    suspend fun addHistoriPembayaran(req: AddHistoriPembayaranRequest) = api.addHistoriPembayaran(req)
    suspend fun decideHistoriPembayaran(req: HistoriPembayaranDecideRequest) =
    api.decideHistoriPembayaran(req)
}