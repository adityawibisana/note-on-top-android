package com.aw.ontopnote.util

import android.content.Context
import android.content.SharedPreferences
import com.aw.ontopnote.MainApp

object SharedPref {
    private const val EMAIL = "email"
    private const val ID = "id"
    private const val PASSWORD = "password"
    private const val TOKEN = "token"

    private val pref : SharedPreferences by lazy {
        MainApp.applicationContext().getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    fun clearAll() {
        pref.edit().clear().apply()
    }

    var email: String?
    get() = pref.getString(EMAIL, null)
    set (value) {
        pref.edit().putString(EMAIL, value).apply()
    }

    var id: String?
    get() = pref.getString(ID, "")
    set (value) {
        pref.edit().putString(ID, value).apply()
    }

    var token: String ?
    get() = pref.getString(TOKEN, null)
    set (value) {
        pref.edit().putString(TOKEN, value).apply()
    }
}