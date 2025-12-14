package com.example.projek_map.utils

import android.content.Context

class PrefManager(context: Context) {

    // ✅ Pertahankan struktur & key lama
    private val PREF_NAME = "koperasi_pref"

    private val KEY_IS_LOGGED_IN = "is_logged_in"
    private val KEY_USER_NAME = "user_name"
    private val KEY_EMAIL = "user_email"
    private val KEY_KODE_PEGAWAI = "kode_pegawai"

    // ✅ Tambahan key untuk fitur pengumuman
    private val KEY_LAST_SEEN_ANNOUNCE = "last_seen_announce"

    // ✅ Key admin (SUDAH ADA, kita pakai dengan benar)
    private val KEY_IS_ADMIN = "is_admin"

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    // ====== API lama (TETAP, TIDAK DIUBAH) ======
    fun saveLogin(userName: String, email: String, kodePegawai: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_NAME, userName)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_KODE_PEGAWAI, kodePegawai)
        editor.apply()
    }

    fun isLoggedIn(): Boolean = pref.getBoolean(KEY_IS_LOGGED_IN, false)
    fun getUserName(): String? = pref.getString(KEY_USER_NAME, "")
    fun getEmail(): String? = pref.getString(KEY_EMAIL, "")
    fun getKodePegawai(): String? = pref.getString(KEY_KODE_PEGAWAI, "")

    // ====== LOGOUT (FIX BUG ROLE NYANGKUT) ======
    fun logout() {
        editor.clear()
        editor.apply()
    }

    // ====== API pengumuman (TETAP) ======
    fun getLastSeenAnnouncementDate(): String? =
        pref.getString(KEY_LAST_SEEN_ANNOUNCE, null)

    fun setLastSeenAnnouncementDate(date: String) {
        editor.putString(KEY_LAST_SEEN_ANNOUNCE, date).apply()
    }

    // ====== FLAG ADMIN (INTI PERBAIKAN) ======
    fun setIsAdmin(isAdmin: Boolean) {
        editor.putBoolean(KEY_IS_ADMIN, isAdmin).apply()
    }

    fun isAdmin(): Boolean = pref.getBoolean(KEY_IS_ADMIN, false)

    // ====== ALIAS kompatibilitas ======
    fun getIsAdmin(): Boolean = isAdmin()
}
