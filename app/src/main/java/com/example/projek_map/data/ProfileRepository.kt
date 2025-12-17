package com.example.projek_map.data

import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.User

class ProfileRepository {

    private val api = ApiClient.apiService

    suspend fun getUser(kodePegawai: String): Result<User> {
        return try {
            val resp = api.getUser(kodePegawai)
            val body = resp.body()

            if (resp.isSuccessful && body?.success == true && body.data != null) {
                Result.success(body.data)
            } else {
                Result.failure(Exception(body?.message ?: "Gagal mengambil data user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
