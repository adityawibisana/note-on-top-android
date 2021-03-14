package com.aw.ontopnote.view

import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager

object ViewHelper {
    private val layoutFlag: Int by lazy {
        if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE
    }

    val defaultTextViewLayoutParams: WindowManager.LayoutParams  by lazy {
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT).apply {
                gravity = Gravity.TOP or Gravity.START or Gravity.CENTER
        }
    }
}