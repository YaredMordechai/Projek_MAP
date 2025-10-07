package com.example.projek_map.utils

import android.content.Context

class PrefManager(context: Context) {

    private val PREF_NAME = "koperasi_pref"

    private val KEY_IS_LOGGED_IN = "is_logged_in"
    private val KEY_USER_NAME = "user_name"
    private val KEY_EMAIL = "user_email"
    private val KEY_KODE_PEGAWAI = "kode_pegawai"

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    // Simpan data login (nama, email, kode pegawai)
    fun saveLogin(userName: String, email: String, kodePegawai: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_NAME, userName)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_KODE_PEGAWAI, kodePegawai)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserName(): String? {
        return pref.getString(KEY_USER_NAME, "")
    }

    fun getEmail(): String? {
        return pref.getString(KEY_EMAIL, "")
    }

    fun getKodePegawai(): String? {
        return pref.getString(KEY_KODE_PEGAWAI, "")
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
