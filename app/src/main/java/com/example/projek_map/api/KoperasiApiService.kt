package com.example.projek_map.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface KoperasiApiService {

    // =========================
    // AUTH (USER)
    // =========================
    @POST("user_login.php")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<ApiResponse<User>>

    @POST("user_register.php")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<ApiResponse<User>>

    // =========================
    // USER
    // =========================
    @GET("users_get.php")
    suspend fun getUser(
        @Query("kodePegawai") kodePegawai: String
    ): Response<ApiResponse<User>>

    @GET("users_get.php")
    suspend fun getAllUsers(): Response<ApiResponse<List<User>>>

    // =========================
    // AUTH (ADMIN)
    // =========================
    @Headers("Content-Type: application/json")
    @POST("admin_login.php")
    suspend fun loginAdmin(
        @Body request: LoginRequest
    ): Response<ApiResponse<Admin>>


    // =========================
    // SIMPANAN
    // =========================
    @GET("simpanan_get.php")
    suspend fun getSimpanan(
        @Query("kodePegawai") kodePegawai: String
    ): Response<ApiResponse<Simpanan?>>

    @POST("simpanan_transaksi.php")
    suspend fun simpananTransaksi(
        @Body req: SimpananTransaksiRequest
    ): Response<ApiResponse<Simpanan?>>

    // =========================
// SIMPANAN PENDING (ADMIN VERIF)
// =========================
    @GET("simpanan_pending_list.php")
    suspend fun getSimpananPending(): Response<ApiResponse<List<SimpananPending>>>

    @POST("simpanan_pending_decide.php")
    suspend fun decideSimpananPending(
        @Body req: SimpananPendingDecideRequest
    ): Response<ApiResponse<Any>>

    @POST("histori_pembayaran_decide.php")
    suspend fun decideHistoriPembayaran(
        @Body req: HistoriPembayaranDecideRequest
    ): Response<ApiResponse<Boolean>>



    // =========================
    // HISTORI SIMPANAN
    // =========================
    @GET("histori_simpanan_get.php")
    suspend fun getHistoriSimpanan(
        @Query("kodePegawai") kodePegawai: String
    ): Response<ApiResponse<List<HistoriSimpanan>>>

    @GET("histori_simpanan_get.php")
    suspend fun getAllHistoriSimpanan(): Response<ApiResponse<List<HistoriSimpanan>>>

    @GET("histori_simpanan_get.php")
    suspend fun getHistoriSimpananByKodePegawai(
        @Query("kodePegawai") kodePegawai: String
    ): Response<ApiResponse<List<HistoriSimpanan>>>

    @GET("simpanan_get_all.php")
    suspend fun getAllSimpanan(): Response<ApiResponse<List<Simpanan>>>

    // =========================
    // BUKTI PEMBAYARAN ANGGOTA
    // =========================
    @POST("bukti_pembayaran_anggota_add.php")
    suspend fun addBuktiPembayaranAnggota(
        @Body req: BuktiPembayaranAnggotaRequest
    ): Response<ApiResponse<Boolean>>

    // =========================
    // PINJAMAN (USER)
    // =========================
    @GET("pinjaman_get.php")
    suspend fun getPinjaman(
        @Query("kodePegawai") kodePegawai: String
    ): Response<ApiResponse<List<Pinjaman>>>

    // ✅ cukup SATU getAllPinjaman, yang di luar tadi dihapus
    @GET("pinjaman_get.php")
    suspend fun getAllPinjaman(): Response<ApiResponse<List<Pinjaman>>>

    @POST("pinjaman_create.php")
    suspend fun createPinjaman(
        @Body req: CreatePinjamanRequest
    ): Response<ApiResponse<Pinjaman>>

    // =========================
    // PINJAMAN (ADMIN)
    // =========================
    @POST("pinjaman_decide.php")
    suspend fun decidePinjaman(
        @Body req: DecidePinjamanRequest
    ): Response<ApiResponse<Boolean>>

    // =========================
    // HISTORI PEMBAYARAN PINJAMAN
    // =========================
    @GET("histori_pembayaran_get.php")
    suspend fun getHistoriPembayaran(
        @Query("pinjamanId") pinjamanId: Int
    ): Response<ApiResponse<List<HistoriPembayaran>>>

    @POST("histori_pembayaran_add.php")
    suspend fun addHistoriPembayaran(
        @Body req: AddHistoriPembayaranRequest
    ): Response<ApiResponse<Boolean>>

    // =========================
    // HISTORI PEMBAYARAN (ADMIN)
    // =========================
    @GET("histori_pembayaran_get.php")
    suspend fun getHistoriPembayaranAdmin(
        @Query("pinjamanId") pinjamanId: Int? = null,
        @Query("kodePegawai") kodePegawai: String? = null
    ): Response<ApiResponse<List<HistoriPembayaran>>>

    @POST("histori_pembayaran_admin_add.php")
    suspend fun catatPembayaranAdmin(
        @Body req: AdminCatatPembayaranRequest
    ): Response<ApiResponse<Boolean>>

    // =========================
    // SETTINGS (PENGATURAN KOPERASI) ✅ pindahan dari luar
    // =========================
    @GET("settings_get.php")
    suspend fun getSettings(): Response<SettingsResponse>

    @POST("settings_update.php")
    suspend fun updateSettings(
        @Body body: SettingsUpdateRequest
    ): Response<ApiResponse<Boolean>>

    // =========================
    // PENGUMUMAN
    // =========================
    @GET("pengumuman_list.php")
    suspend fun getPengumuman(): Response<ApiResponse<List<Pengumuman>>>

    @POST("pengumuman_add.php")
    suspend fun addPengumuman(
        @Body req: PengumumanAddRequest
    ): Response<ApiResponse<Pengumuman>>

    @POST("pengumuman_update.php")
    suspend fun updatePengumuman(
        @Body req: PengumumanUpdateRequest
    ): Response<ApiResponse<Boolean>>

    @POST("pengumuman_delete.php")
    suspend fun deletePengumuman(
        @Body req: PengumumanDeleteRequest
    ): Response<ApiResponse<Boolean>>


    // =========================
    // NOTIFIKASI
    // =========================
    @GET("decision_notifications_get.php")
    suspend fun getDecisionNotifications(
        @Query("kodePegawai") kodePegawai: String,
        @Query("onlyUnread") onlyUnread: Int = 1
    ): Response<ApiResponse<List<DecisionNotificationDto>>>

    @POST("decision_notifications_mark_read.php")
    suspend fun markDecisionNotificationsRead(
        @Body req: MarkReadRequest
    ): Response<ApiResponse<Boolean>>

    // =========================
    // USERS (ADMIN CRUD)
    // =========================
    @POST("users_add.php")
    suspend fun addUser(@Body req: AddUserRequest): Response<ApiResponse<User>>

    @POST("users_update.php")
    suspend fun updateUser(@Body req: UpdateUserRequest): Response<ApiResponse<Boolean>>

    @POST("users_set_status.php")
    suspend fun setUserStatus(@Body req: SetUserStatusRequest): Response<ApiResponse<Boolean>>

    @POST("users_delete.php")
    suspend fun deleteUser(@Body req: DeleteUserRequest): Response<ApiResponse<Boolean>>

    // =========================
    // KAS (ADMIN)
    // =========================
    @GET("kas_get.php")
    suspend fun getKas(): Response<ApiResponse<List<KasTransaksi>>>

    @POST("kas_add.php")
    suspend fun addKas(@Body req: KasTransaksiAddRequest): Response<ApiResponse<Map<String, Int>>>

    @POST("kas_update.php")
    suspend fun updateKas(@Body req: KasTransaksiUpdateRequest): Response<ApiResponse<Boolean>>

    @POST("kas_delete.php")
    suspend fun deleteKas(@Body req: KasDeleteRequest): Response<ApiResponse<Boolean>>

    // =========================
// LAPORAN
// =========================
    @GET("laporan_user.php")
    suspend fun getLaporanUser(
        @Query("kodePegawai") kodePegawai: String,
        @Query("bulan") bulan: Int,
        @Query("year") year: Int
    ): Response<ApiResponse<LaporanUserData>>

    @GET("laporan_admin.php")
    suspend fun getLaporanAdmin(): Response<LaporanAdminResponse>

    // =========================
// LABA (ADMIN)
// =========================
    @GET("laba_bersih.php")
    suspend fun getLabaBersih(): Response<ApiResponse<LabaBersihData>>

    @GET("laba_rugi.php")
    suspend fun getLabaRugi(
        @Query("year") year: Int,
        @Query("bulan") bulan: Int = 0
    ): Response<LabaRugiResponse>

    // ===== PINJAMAN RINCIAN & JADWAL (DB) =====
    @GET("pinjaman_rincian_get.php")
    suspend fun getPinjamanRincian(
        @Query("pinjamanId") pinjamanId: Int,
        @Query("metode") metode: String // "anuitas" | "flat"
    ): Response<ApiResponse<RincianPinjaman>>

    @GET("pinjaman_jadwal_get.php")
    suspend fun getPinjamanJadwal(
        @Query("pinjamanId") pinjamanId: Int,
        @Query("metode") metode: String // "anuitas" | "flat"
    ): Response<ApiResponse<List<AngsuranItem>>>






}
