package com.aw.ontopnote

import android.app.Application
import android.content.Context
import timber.log.Timber
import timber.log.Timber.DebugTree



class MainApp: Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MainApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }  
}