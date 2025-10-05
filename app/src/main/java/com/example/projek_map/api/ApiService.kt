package com.example.projek_map.api

import com.example.projek_map.model.Pinjaman
import com.example.projek_map.model.PinjamanResponse
import com.example.projek_map.model.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // GET data pinjaman (riwayat pinjaman)
    @GET("get_pinjaman.php")
    fun getPinjaman(): Call<List<Pinjaman>>

    // POST tambah pinjaman
    @FormUrlEncoded
    @POST("insert_pinjaman.php")
    fun insertPinjaman(
        @Field("jumlah") jumlah: String,
        @Field("tenor") tenor: String
    ): Call<PinjamanResponse>


    @FormUrlEncoded
    @POST("get_user.php")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("register.php")
    fun registerUser(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserResponse>

}
