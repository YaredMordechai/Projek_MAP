package com.example.projek_map.api

import com.example.projek_map.data.Pengumuman
import com.example.projek_map.data.Pinjaman
import com.example.projek_map.data.Simpanan
import com.example.projek_map.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface KoperasiApiService {

    // LOGIN
    @POST("user_login.php")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<User>>

    // REGISTER
    @POST("user_register.php")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<User>>

    // GET 1 user
    @GET("users_get.php")
    suspend fun getUser(
        @Query("kodePegawai") kodePegawai: String
    ): Response<ApiResponse<User>>

    // GET semua user
    @GET("users_get.php")
    suspend fun getAllUsers(): Response<ApiResponse<List<User>>>

    // SIMPANAN per user
    @GET("simpanan_get.php")
    suspend fun getSimpanan(
        @Query("kodePegawai") kodePegawai: String
    ): Response<ApiResponse<Simpanan?>>

    // PINJAMAN per user
    @GET("pinjaman_get.php")
    suspend fun getPinjaman(
        @Query("kodePegawai") kodePegawai: String
    ): Response<ApiResponse<List<Pinjaman>>>

    // PENGUMUMAN
    @GET("pengumuman_list.php")
    suspend fun getPengumuman(): Response<ApiResponse<List<Pengumuman>>>
}

// ==== Models untuk request/response API ====

data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val kodePegawai: String,
    val email: String,
    val password: String,
    val nama: String
)
