package com.example.projek_map.api

import com.example.projek_map.data.Pinjaman
import com.example.projek_map.model.PinjamanResponse
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

}
