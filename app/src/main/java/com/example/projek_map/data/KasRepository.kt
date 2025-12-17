package com.example.projek_map.data

import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.KasDeleteRequest
import com.example.projek_map.api.KasTransaksiAddRequest
import com.example.projek_map.api.KasTransaksiUpdateRequest
import com.example.projek_map.api.KoperasiApiService

class KasRepository(
    private val api: KoperasiApiService = ApiClient.apiService
) {
    suspend fun getKas() = api.getKas()
    suspend fun addKas(req: KasTransaksiAddRequest) = api.addKas(req)
    suspend fun updateKas(req: KasTransaksiUpdateRequest) = api.updateKas(req)
    suspend fun deleteKas(req: KasDeleteRequest) = api.deleteKas(req)
}
