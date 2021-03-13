package com.aw.ontopnote.view

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.aw.ontopnote.model.Note

class ViewManager(private val context: Context, private val windowManager: WindowManager, initialNoteLiveData: LiveData<Note>?) {

    private val defaultTextView: DefaultTextView by lazy {
        DefaultTextView.getInstance(context)
    }

    private val mLayoutParams: WindowManager.LayoutParams  by lazy {
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT)
    }

    private val textViews = ArrayList<TextView>()

    private val layoutFlag: Int by lazy {
        if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE
    }

    init {
        mLayoutParams.gravity = Gravity.START or Gravity.TOP
        initialNoteLiveData?.run {
            addTextViewToWindowManager(this)
        }
    }

    private fun addTextViewToWindowManager(noteLiveData: LiveData<Note>) {
        val textView = defaultTextView.generateTextView(noteLiveData)
        windowManager.addView(textView, mLayoutParams)
        textViews.add(textView)
    }

    fun removeAll() {
        textViews.forEach {
            windowManager.removeView(it)
        }
    }
}