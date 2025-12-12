package com.example.projek_map.data

import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.ApiResponse
import com.example.projek_map.api.LoginRequest
import com.example.projek_map.api.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {

    private val api = ApiClient.apiService

    suspend fun login(email: String, password: String): ApiResponse<User> {
        return withContext(Dispatchers.IO) {
            try {
                val resp = api.login(LoginRequest(email, password))
                if (resp.isSuccessful) {
                    resp.body() ?: ApiResponse(false, "Response kosong dari server", null)
                } else {
                    ApiResponse(false, "HTTP ${resp.code()} ${resp.message()}", null)
                }
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal konek ke server", null)
            }
        }
    }

    suspend fun register(
        kodePegawai: String,
        email: String,
        password: String,
        nama: String
    ): ApiResponse<User> {
        return withContext(Dispatchers.IO) {
            try {
                val resp = api.register(RegisterRequest(kodePegawai, email, password, nama))
                if (resp.isSuccessful) {
                    resp.body() ?: ApiResponse(false, "Response kosong dari server", null)
                } else {
                    ApiResponse(false, "HTTP ${resp.code()} ${resp.message()}", null)
                }
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Gagal konek ke server", null)
            }
        }
    }
}
