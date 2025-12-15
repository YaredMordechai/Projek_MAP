package com.example.projek_map.data

import com.example.projek_map.api.AdminCatatPembayaranRequest
import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.ApiResponse
import com.example.projek_map.api.HistoriPembayaran
import com.example.projek_map.api.KoperasiApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoriPembayaranAdminRepository(
    private val api: KoperasiApiService = ApiClient.apiService
) {

    suspend fun getAll(): ApiResponse<List<HistoriPembayaran>> =
        withContext(Dispatchers.IO) {
            try {
                val res = api.getHistoriPembayaranAdmin()

                if (res.isSuccessful) {
                    val body: ApiResponse<List<HistoriPembayaran>>? = res.body()
                    body ?: ApiResponse(false, "Response kosong", emptyList<HistoriPembayaran>())
                } else {
                    ApiResponse(false, "HTTP ${res.code()} ${res.message()}", emptyList<HistoriPembayaran>())
                }
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal ambil histori", emptyList<HistoriPembayaran>())
            }
        }

    suspend fun catat(kode: String, pinjamanId: Int, jumlah: Int): ApiResponse<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID")).format(Date())
                val res = api.catatPembayaranAdmin(
                    AdminCatatPembayaranRequest(
                        kodePegawai = kode,
                        pinjamanId = pinjamanId,
                        tanggal = tanggal,
                        jumlah = jumlah,
                        status = "Dibayar (Admin)"
                    )
                )

                if (res.isSuccessful) {
                    res.body() ?: ApiResponse(false, "Response kosong", false)
                } else {
                    ApiResponse(false, "HTTP ${res.code()} ${res.message()}", false)
                }
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal catat pembayaran", false)
            }
        }
}
