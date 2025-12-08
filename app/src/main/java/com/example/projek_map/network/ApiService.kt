package com.example.projek_map.network

import com.example.projek_map.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val role: String?,
    val user: User?,
    val admin: Admin?
)

data class SimpleResponse(
    val success: Boolean,
    val message: String?
)

data class SimpananResponse(
    val success: Boolean,
    val message: String?,
    val simpanan: Simpanan?,
    val histori: List<HistoriSimpanan>?,
    val transaksi: List<TransaksiSimpanan>?
)

data class PinjamanListResponse(
    val success: Boolean,
    val message: String?,
    val pinjaman: List<Pinjaman>?
)

data class HistoriPembayaranListResponse(
    val success: Boolean,
    val message: String?,
    val histori: List<HistoriPembayaran>?
)

data class PengumumanListResponse(
    val success: Boolean,
    val message: String?,
    val pengumuman: List<Pengumuman>?
)

data class AddPengumumanResponse(
    val success: Boolean,
    val message: String?,
    val pengumuman: Pengumuman?
)

interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("signup.php")
    suspend fun signup(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<SimpleResponse>

    @GET("get_simpanan.php")
    suspend fun getSimpanan(
        @Query("kodePegawai") kodePegawai: String
    ): Response<SimpananResponse>

    @FormUrlEncoded
    @POST("tambah_simpanan.php")
    suspend fun tambahSimpanan(
        @Field("kodePegawai") kodePegawai: String,
        @Field("jenis") jenis: String,
        @Field("jumlah") jumlah: Double,
        @Field("keterangan") keterangan: String
    ): Response<SimpleResponse>

    @GET("get_pinjaman_user.php")
    suspend fun getPinjamanUser(
        @Query("kodePegawai") kodePegawai: String
    ): Response<PinjamanListResponse>

    @FormUrlEncoded
    @POST("ajukan_pinjaman.php")
    suspend fun ajukanPinjaman(
        @Field("kodePegawai") kodePegawai: String,
        @Field("jumlah") jumlah: Int,
        @Field("tenor") tenor: Int
    ): Response<SimpleResponse>

    @GET("get_histori_pembayaran.php")
    suspend fun getHistoriPembayaran(
        @Query("pinjamanId") pinjamanId: Int
    ): Response<HistoriPembayaranListResponse>

    @Multipart
    @POST("upload_bukti_pembayaran.php")
    suspend fun uploadBuktiPembayaran(
        @Part("kodePegawai") kodePegawai: RequestBody,
        @Part("pinjamanId") pinjamanId: RequestBody,
        @Part("jumlah") jumlah: RequestBody,
        @Part("status") status: RequestBody,
        @Part bukti: MultipartBody.Part
    ): Response<SimpleResponse>

    @GET("get_pengumuman.php")
    suspend fun getPengumuman(): Response<PengumumanListResponse>

    @FormUrlEncoded
    @POST("add_pengumuman.php")
    suspend fun addPengumuman(
        @Field("judul") judul: String,
        @Field("isi") isi: String
    ): Response<AddPengumumanResponse>
}
