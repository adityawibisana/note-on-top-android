package com.aw.ontopnote.view

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.aw.ontopnote.event.LastEditedNoteChanged
import com.aw.ontopnote.model.Note
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ViewManager(private val context: Context, private val windowManager: WindowManager) {

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
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLastEditedNoteChanged(lastEditedNoteChanged: LastEditedNoteChanged) {
        val note = lastEditedNoteChanged.note
        if (textViews.size == 0) {
            addTextViewToWindowManager(note)
        } else {
            val found = textViews.find {
                (it.tag as Note).id == note.id
            }

            if (found == null) {
                //  remove all of oldview
                for (textView in textViews) {
                    windowManager.removeView(textView)
                }

                addTextViewToWindowManager(note)
            } else {
                windowManager.updateViewLayout(
                    defaultTextView.decorateTextView(found, note),
                    mLayoutParams
                )
            }
        }
    }

    private fun addTextViewToWindowManager(note: Note) {
        val textView = defaultTextView.generateTextView(note)
        windowManager.addView(textView, mLayoutParams)
        textViews.add(textView)
    }

}