package com.aw.commons

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

class AndroidUIHelper private constructor(context: Context) {
    companion object : SingletonHolder<AndroidUIHelper, Context>(::AndroidUIHelper)

    val screenHeightInPixel: Int by lazy {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        metrics.heightPixels
    }

    val screenWidthInPixel: Int by lazy {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        metrics.widthPixels
    }

    fun intToColorHex(color: Int) = String.format("#%06X", 0xFFFFFF and color)

}