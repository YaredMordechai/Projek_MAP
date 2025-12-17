package com.example.projek_map.data

import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.PengumumanAddRequest
import com.example.projek_map.api.PengumumanDeleteRequest
import com.example.projek_map.api.PengumumanUpdateRequest
import com.example.projek_map.api.Pengumuman

class PengumumanRepository {

    private val api = ApiClient.apiService

    suspend fun getPengumuman(): Result<List<Pengumuman>> {
        return try {
            val resp = api.getPengumuman()
            val body = resp.body()
            if (resp.isSuccessful && body?.success == true) {
                Result.success(body.data ?: emptyList())
            } else {
                Result.failure(Exception(body?.message ?: "Gagal memuat pengumuman"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addPengumuman(req: PengumumanAddRequest): Result<Unit> {
        return try {
            val resp = api.addPengumuman(req)
            val body = resp.body()
            if (resp.isSuccessful && body?.success == true) Result.success(Unit)
            else Result.failure(Exception(body?.message ?: "Gagal tambah pengumuman"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePengumuman(req: PengumumanUpdateRequest): Result<Unit> {
        return try {
            val resp = api.updatePengumuman(req)
            val body = resp.body()
            if (resp.isSuccessful && body?.success == true) Result.success(Unit)
            else Result.failure(Exception(body?.message ?: "Gagal update pengumuman"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePengumuman(req: PengumumanDeleteRequest): Result<Unit> {
        return try {
            val resp = api.deletePengumuman(req)
            val body = resp.body()
            if (resp.isSuccessful && body?.success == true) Result.success(Unit)
            else Result.failure(Exception(body?.message ?: "Gagal hapus pengumuman"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
