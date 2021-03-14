package com.aw.ontopnote.view

import android.content.Context
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.aw.ontopnote.event.WindowManagerLayoutParamsChanged
import com.aw.ontopnote.model.Note
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ViewManager(private val context: Context, private val windowManager: WindowManager, initialNoteLiveData: LiveData<Note>?) {

    private val defaultTextView: DefaultTextView by lazy {
        DefaultTextView.getInstance(context)
    }

    private val textViews = ArrayList<TextView>()

    init {
        EventBus.getDefault().register(this)
        initialNoteLiveData?.run {
            addTextViewToWindowManager(this)
        }
    }

    private fun addTextViewToWindowManager(noteLiveData: LiveData<Note>) {
        val textView = defaultTextView.generateTextView(noteLiveData)
        windowManager.addView(textView, ViewHelper.defaultTextViewLayoutParams)
        textViews.add(textView)
    }

    fun removeAll() {
        textViews.forEach {
            windowManager.removeView(it)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onViewWindowManagerLayoutChanged(event: WindowManagerLayoutParamsChanged) {
        windowManager.updateViewLayout(event.view, event.layoutParams)
    }
}