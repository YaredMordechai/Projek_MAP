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
import retrofit2.http.Headers

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

    // AUTH (ADMIN)
    @Headers("Content-Type: application/json")
    @POST("admin_login.php")
    suspend fun loginAdmin(
        @Body request: LoginRequest
    ): Response<ApiResponse<com.example.projek_map.data.Admin>>


    // SIMPANAN
    @GET("simpanan_get.php")
    suspend fun getSimpanan(@Query("kodePegawai") kodePegawai: String): Response<ApiResponse<Simpanan?>>

    // SIMPANAN - TRANSAKSI SETOR / TARIK (update simpanan + insert histori)
    @POST("simpanan_transaksi.php")
    suspend fun simpananTransaksi(@Body req: SimpananTransaksiRequest): Response<ApiResponse<Simpanan>>

    // BUKTI PEMBAYARAN ANGGOTA (upload uri bukti)
    @POST("bukti_pembayaran_anggota_add.php")
    suspend fun addBuktiPembayaranAnggota(@Body req: BuktiPembayaranAnggotaRequest): Response<ApiResponse<Boolean>>

    // optional: versi filter histori (tetap biarkan getAllHistoriSimpanan() yang lama)
    @GET("histori_simpanan_get.php")
    suspend fun getHistoriSimpananByKodePegawai(
        @Query("kodePegawai") kodePegawai: String
    ): Response<ApiResponse<List<HistoriSimpanan>>>


    // PINJAMAN
    @GET("pinjaman_get.php")
    suspend fun getPinjaman(@Query("kodePegawai") kodePegawai: String): Response<ApiResponse<List<Pinjaman>>>

    @GET("pinjaman_get.php")
    suspend fun getAllPinjaman(): Response<ApiResponse<List<Pinjaman>>>

    @POST("pinjaman_create.php")
    suspend fun createPinjaman(@Body req: CreatePinjamanRequest): Response<ApiResponse<Pinjaman>>

    //PINJAMAN ADMIN
    @POST("pinjaman_decide.php")
    suspend fun decidePinjaman(@Body req: DecidePinjamanRequest): Response<ApiResponse<Boolean>>


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

    //NOTIFIKASI
    @GET("decision_notifications_get.php")
    suspend fun getDecisionNotifications(
        @Query("kodePegawai") kodePegawai: String,
        @Query("onlyUnread") onlyUnread: Int = 1
    ): Response<ApiResponse<List<DecisionNotificationDto>>>

    @POST("decision_notifications_mark_read.php")
    suspend fun markDecisionNotificationsRead(
        @Body req: MarkReadRequest
    ): Response<ApiResponse<Boolean>>

//HISTORI PEMBAYARAN
@GET("histori_pembayaran_get.php")
suspend fun getHistoriPembayaranAdmin(
    @Query("pinjamanId") pinjamanId: Int? = null,
    @Query("kodePegawai") kodePegawai: String? = null
): Response<ApiResponse<List<Map<String, Any?>>>>

    @POST("histori_pembayaran_admin_add.php")
    suspend fun catatPembayaranAdmin(
        @Body req: AdminCatatPembayaranRequest
    ): Response<ApiResponse<Boolean>>


}
