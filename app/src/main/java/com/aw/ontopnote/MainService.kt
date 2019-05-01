package com.aw.ontopnote

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager

class MainService : Service() {

    private lateinit var mView: MainView
    private lateinit var mWindowManager: WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        val layoutFlag = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE

        mLayoutParams = WindowManager.LayoutParams(
              WindowManager.LayoutParams.WRAP_CONTENT,
              WindowManager.LayoutParams.WRAP_CONTENT,
              layoutFlag,
              WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                      or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                      or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        mLayoutParams.gravity = Gravity.TOP or Gravity.START

        if (!this::mWindowManager.isInitialized) {
            mWindowManager = (getSystemService( Context.WINDOW_SERVICE) as WindowManager)
            mView = MainView(applicationContext)

            mWindowManager.addView(this.mView, mLayoutParams)
        }
    }
}