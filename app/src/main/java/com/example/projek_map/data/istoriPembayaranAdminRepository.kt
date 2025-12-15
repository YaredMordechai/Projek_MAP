package com.example.projek_map.data

import com.example.projek_map.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoriPembayaranAdminRepository(
    private val api: KoperasiApiService = ApiClient.apiService
) {
    suspend fun getAll(): ApiResponse<List<Map<String, Any?>>> =
        withContext(Dispatchers.IO) {
            try {
                val res = api.getHistoriPembayaranAdmin()
                if (res.isSuccessful) res.body() ?: ApiResponse(false, "Response kosong", emptyList())
                else ApiResponse(false, "HTTP ${res.code()} ${res.message()}", emptyList())
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal ambil histori", emptyList())
            }
        }

    suspend fun catat(kode: String, pinjamanId: Int, jumlah: Int): ApiResponse<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                val res = api.catatPembayaranAdmin(
                    AdminCatatPembayaranRequest(kode, pinjamanId, tanggal, jumlah)
                )
                if (res.isSuccessful) res.body() ?: ApiResponse(false, "Response kosong", false)
                else ApiResponse(false, "HTTP ${res.code()} ${res.message()}", false)
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal catat pembayaran", false)
            }
        }
}
