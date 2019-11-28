package com.aw.ontopnote

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
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
        val context: Context = MainApp.applicationContext()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Fabric.with(context, Crashlytics())
        }
    }  
}