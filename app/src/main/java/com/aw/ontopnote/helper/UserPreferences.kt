package com.aw.ontopnote.helper

import android.content.Context
import android.content.SharedPreferences
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.R

object UserPreferences {
    private const val USER_PREF = "userPref"

    private val userPref: SharedPreferences by lazy {
        MainApp.applicationContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
    }

    private const val NOTE_PADDING_SIZE = "notePaddingSize"

    fun getNotePaddingSize() : Int {
        return userPref.getInt(NOTE_PADDING_SIZE, MainApp.applicationContext().resources.getDimension(R.dimen.margin).toInt())
    }

    fun setNotePaddingSize(size: Int) {
        userPref.edit().putInt(NOTE_PADDING_SIZE, size).apply()
    }
}