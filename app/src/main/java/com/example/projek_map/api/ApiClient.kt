package com.example.projek_map.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // Emulator Android Studio
    private const val BASE_URL = "http://10.0.2.2/koperasi_api/"

    // ===== logging bawaan (request + response body) =====
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ===== TAMBAHAN: interceptor khusus ERROR BODY =====
    private val errorBodyInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val body = response.peekBody(1024 * 1024).string()
            Log.e(
                "API_ERROR_BODY",
                "HTTP ${response.code} ${request.url}\n$body"
            )
        }

        response
    }

    // ===== OkHttpClient (STRUKTUR TIDAK DIUBAH) =====
    private val client = OkHttpClient.Builder()
        .addInterceptor(errorBodyInterceptor) // ← TAMBAHAN SAJA
        .addInterceptor(logging)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    val apiService: KoperasiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KoperasiApiService::class.java)
    }
}
