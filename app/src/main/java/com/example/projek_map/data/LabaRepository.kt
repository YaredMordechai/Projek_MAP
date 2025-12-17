package com.example.projek_map.data

import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.KoperasiApiService

class LabaRepository(
    private val api: KoperasiApiService = ApiClient.apiService
) {
    suspend fun getLabaBersih() = api.getLabaBersih()
    suspend fun getLabaRugi(year: Int, bulan: Int) = api.getLabaRugi(year = year, bulan = bulan)
}
