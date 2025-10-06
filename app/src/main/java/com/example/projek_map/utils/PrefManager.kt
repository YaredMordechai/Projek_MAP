package com.example.projek_map.utils

import android.content.Context

class PrefManager(context: Context) {

    private val PREF_NAME = "koperasi_pref"
    private val KEY_IS_LOGGED_IN = "is_logged_in"
    private val KEY_USER_NAME = "user_name"

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun saveLogin(userName: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_NAME, userName)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserName(): String? {
        return pref.getString(KEY_USER_NAME, "")
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}
