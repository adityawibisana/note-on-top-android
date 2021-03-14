package com.aw.ontopnote.view

import android.content.Context
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.aw.ontopnote.model.Note

class ViewManager(private val context: Context, private val windowManager: WindowManager, initialNoteLiveData: LiveData<Note>?) {

    private val defaultTextView: DefaultTextView by lazy {
        DefaultTextView.getInstance(context)
    }

    private val textViews = ArrayList<TextView>()

    init {
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
}