package com.example.projek_map.data

import com.example.projek_map.api.ApiClient
import com.example.projek_map.api.ApiResponse
import com.example.projek_map.api.KoperasiApiService
import com.example.projek_map.api.LoginRequest
import com.example.projek_map.api.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val api: KoperasiApiService = ApiClient.apiService
) {

    suspend fun login(email: String, password: String): ApiResponse<User>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()
                } else {
                    ApiResponse(false, "Error code: ${response.code()}", null)
                }
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Unknown error", null)
            }
        }
    }

    suspend fun register(
        kodePegawai: String,
        email: String,
        password: String,
        nama: String
    ): ApiResponse<User>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.register(
                    RegisterRequest(
                        kodePegawai = kodePegawai,
                        email = email,
                        password = password,
                        nama = nama
                    )
                )
                if (response.isSuccessful) {
                    response.body()
                } else {
                    ApiResponse(false, "Error code: ${response.code()}", null)
                }
            } catch (e: Exception) {
                ApiResponse(false, e.message ?: "Unknown error", null)
            }
        }
    }
}