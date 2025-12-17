package com.example.projek_map.data

import com.example.projek_map.api.ApiClient

class LaporanRepository {

    private val api = ApiClient.apiService

    suspend fun getLaporanUser(kodePegawai: String, bulan: Int, tahun: Int): Result<Any> {
        return try {
            // ✅ FIX: pakai year = tahun (sesuai nama param di interface Retrofit)
            val resp = api.getLaporanUser(kodePegawai = kodePegawai, bulan = bulan, year = tahun)
            val body = resp.body()
            if (resp.isSuccessful && body?.success == true && body.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(Exception(body?.message ?: "Gagal memuat laporan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
