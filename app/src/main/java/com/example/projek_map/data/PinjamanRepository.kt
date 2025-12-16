package com.example.projek_map.data

import com.example.projek_map.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PinjamanRepository {

    private val api = ApiClient.apiService

    suspend fun getPinjamanUser(kodePegawai: String): ApiResponse<List<Pinjaman>> {
        return withContext(Dispatchers.IO) {
            try {
                val resp = api.getPinjaman(kodePegawai)
                if (resp.isSuccessful) resp.body() ?: ApiResponse(false, "Response kosong", emptyList())
                else ApiResponse(false, "HTTP ${resp.code()} ${resp.message()}", emptyList())
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal konek ke server", emptyList())
            }
        }
    }

    suspend fun createPinjaman(kodePegawai: String, jumlah: Int, tenor: Int): ApiResponse<Pinjaman?> {
        return withContext(Dispatchers.IO) {
            try {
                val resp = api.createPinjaman(CreatePinjamanRequest(kodePegawai, jumlah, tenor))
                if (resp.isSuccessful) ApiResponse(true, resp.body()?.message, resp.body()?.data)
                else ApiResponse(false, "HTTP ${resp.code()} ${resp.message()}", null)
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal konek ke server", null)
            }
        }
    }

    suspend fun addHistoriBukti(
        kodePegawai: String,
        pinjamanId: Int,
        jumlah: Int,
        status: String,
        buktiBase64: String?,
        buktiExt: String?
    ): ApiResponse<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                val resp = api.addHistoriPembayaran(
                    AddHistoriPembayaranRequest(
                        kodePegawai = kodePegawai,
                        pinjamanId = pinjamanId,
                        tanggal = tanggal,
                        jumlah = jumlah,
                        status = status,
                        buktiPembayaranUri = null,  // ✅ jangan pakai uri lokal
                        buktiBase64 = buktiBase64,
                        buktiExt = buktiExt ?: "jpg"
                    )
                )
                if (resp.isSuccessful) resp.body() ?: ApiResponse(false, "Response kosong", false)
                else ApiResponse(false, "HTTP ${resp.code()} ${resp.message()}", false)
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal konek ke server", false)
            }
        }
    }

    suspend fun getAllPinjamanAdmin(): ApiResponse<List<Pinjaman>> {
        return withContext(Dispatchers.IO) {
            try {
                val resp = api.getAllPinjaman()
                if (resp.isSuccessful) resp.body() ?: ApiResponse(false, "Response kosong", emptyList())
                else ApiResponse(false, "HTTP ${resp.code()} ${resp.message()}", emptyList())
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal konek ke server", emptyList())
            }
        }
    }

    suspend fun getRincianPinjamanDb(pinjamanId: Int, metode: String): ApiResponse<RincianPinjaman> {
        return try {
            val resp = ApiClient.apiService.getPinjamanRincian(pinjamanId, metode)
            resp.body() ?: ApiResponse(false, "Empty response", null)
        } catch (e: Exception) {
            ApiResponse(false, "Error: ${e.message}", null)
        }
    }

    suspend fun getJadwalPinjamanDb(pinjamanId: Int, metode: String): ApiResponse<List<AngsuranItem>> {
        return try {
            val resp = ApiClient.apiService.getPinjamanJadwal(pinjamanId, metode)
            resp.body() ?: ApiResponse(false, "Empty response", null)
        } catch (e: Exception) {
            ApiResponse(false, "Error: ${e.message}", null)
        }
    }


}
