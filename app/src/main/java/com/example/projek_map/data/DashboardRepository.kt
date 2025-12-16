package com.example.projek_map.data

import com.example.projek_map.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class DashboardTotals(
    val totalSimpanan: Double,
    val totalPinjaman: Double
)

class DashboardRepository {

    private val api = ApiClient.apiService

    suspend fun getUserTotals(kodePegawai: String): Result<DashboardTotals> {
        return withContext(Dispatchers.IO) {
            try {
                val simpananResp = api.getSimpanan(kodePegawai)
                val pinjamanResp = api.getPinjaman(kodePegawai)

                val simpananBody = simpananResp.body()
                val pinjamanBody = pinjamanResp.body()

                val totalSimpanan =
                    if (simpananResp.isSuccessful && simpananBody?.success == true && simpananBody.data != null) {
                        simpananBody.data.simpananPokok +
                                simpananBody.data.simpananWajib +
                                simpananBody.data.simpananSukarela
                    } else 0.0

                val totalPinjaman =
                    if (pinjamanResp.isSuccessful && pinjamanBody?.success == true) {
                        pinjamanBody.data.orEmpty().sumOf { it.jumlah.toDouble() }
                    } else 0.0

                Result.success(DashboardTotals(totalSimpanan, totalPinjaman))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
