package com.example.projek_map.api

import com.example.projek_map.data.HistoriSimpanan
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

    // AUTH
    @POST("user_login.php")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<User>>

    @POST("user_register.php")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<User>>

    // USER
    @GET("users_get.php")
    suspend fun getUser(@Query("kodePegawai") kodePegawai: String): Response<ApiResponse<User>>

    @GET("users_get.php")
    suspend fun getAllUsers(): Response<ApiResponse<List<User>>>

    // SIMPANAN
    @GET("simpanan_get.php")
    suspend fun getSimpanan(@Query("kodePegawai") kodePegawai: String): Response<ApiResponse<Simpanan?>>

    // PINJAMAN
    @GET("pinjaman_get.php")
    suspend fun getPinjaman(@Query("kodePegawai") kodePegawai: String): Response<ApiResponse<List<Pinjaman>>>

    @GET("pinjaman_get.php")
    suspend fun getAllPinjaman(): Response<ApiResponse<List<Pinjaman>>>

    @POST("pinjaman_create.php")
    suspend fun createPinjaman(@Body req: CreatePinjamanRequest): Response<ApiResponse<Pinjaman>>

    // HISTORI SIMPANAN
    @GET("histori_simpanan_get.php")
    suspend fun getHistoriSimpanan(@Query("kodePegawai") kodePegawai: String): Response<ApiResponse<List<HistoriSimpanan>>>

    @GET("histori_simpanan_get.php")
    suspend fun getAllHistoriSimpanan(): Response<ApiResponse<List<HistoriSimpanan>>>

    // HISTORI PEMBAYARAN PINJAMAN
    @GET("histori_pembayaran_get.php")
    suspend fun getHistoriPembayaran(@Query("pinjamanId") pinjamanId: Int): Response<ApiResponse<List<Map<String, Any?>>>>

    @POST("histori_pembayaran_add.php")
    suspend fun addHistoriPembayaran(@Body req: AddHistoriPembayaranRequest): Response<ApiResponse<Boolean>>

    // PENGUMUMAN
    @GET("pengumuman_list.php")
    suspend fun getPengumuman(): Response<ApiResponse<List<Pengumuman>>>
}
