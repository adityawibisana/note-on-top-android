package com.aw.ontopnote.view

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.aw.ontopnote.R
import com.aw.ontopnote.event.MenuVisibilityChanged
import com.aw.ontopnote.event.WindowManagerLayoutParamsChanged
import com.aw.ontopnote.model.Note
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ViewManager(private val context: Context, private val windowManager: WindowManager, initialNoteLiveData: LiveData<Note>?) {

    private val inflater: LayoutInflater by lazy {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
    }

    private val menu: View by lazy {
        inflater.inflate(R.layout.view_menu, null).also {
            it.visibility = View.GONE
        }
    }

    private val defaultTextView: DefaultTextView by lazy {
        DefaultTextView.getInstance(context)
    }

    private val textViews = ArrayList<TextView>()

    init {
        EventBus.getDefault().register(this)
        initialNoteLiveData?.run {
            addTextViewToWindowManager(this)
        }
        addMenu()
    }

    private fun addTextViewToWindowManager(noteLiveData: LiveData<Note>) {
        val textView = defaultTextView.generateTextView(noteLiveData)
        windowManager.addView(textView, ViewHelper.defaultTextViewLayoutParams)
        textViews.add(textView)
    }

    private fun addMenu() {
        val layout = ViewHelper.defaultTextViewLayoutParams
        layout.apply {
            gravity = Gravity.BOTTOM or Gravity.END
        }
        windowManager.addView(menu, layout)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMenuTriggered(event: MenuVisibilityChanged) {
        menu.visibility = if (menu.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}