package com.example.projek_map.utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences("koperasi_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_IS_ADMIN = "is_admin"
        private const val KEY_KODE_PEGAWAI = "kode_pegawai"
        private const val KEY_EMAIL = "email"
        private const val KEY_NAMA = "nama"
        private const val KEY_LAST_LOGIN = "last_login"
    }

    fun saveLogin(
        kodePegawai: String,
        email: String,
        nama: String,
        isAdmin: Boolean
    ) {
        pref.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putBoolean(KEY_IS_ADMIN, isAdmin)
            .putString(KEY_KODE_PEGAWAI, kodePegawai)
            .putString(KEY_EMAIL, email)
            .putString(KEY_NAMA, nama)
            .apply()
    }

    fun isLoggedIn(): Boolean = pref.getBoolean(KEY_IS_LOGGED_IN, false)

    fun isAdmin(): Boolean = pref.getBoolean(KEY_IS_ADMIN, false)

    fun getKodePegawai(): String? = pref.getString(KEY_KODE_PEGAWAI, null)

    fun getEmail(): String? = pref.getString(KEY_EMAIL, null)

    fun getUserName(): String? = pref.getString(KEY_NAMA, null)

    fun saveLastLogin(time: String) {
        pref.edit().putString(KEY_LAST_LOGIN, time).apply()
    }

    fun getLastLogin(): String? = pref.getString(KEY_LAST_LOGIN, "-")

    fun logout() {
        pref.edit().clear().apply()
    }
}
