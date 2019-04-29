package com.aw.ontopnote

import android.app.Application
import android.content.Context
import android.util.Log

class MainApp: Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MainApp? = null
        val TAG = "MainApp"

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = MainApp.applicationContext()

        Log.v(TAG, "onCreate")
    }  
}