package com.aw.ontopnote

import android.animation.ObjectAnimator
import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.*
import androidx.core.view.setMargins
import com.aw.ontopnote.model.event.FirstNoteEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import android.util.Log

class MainView(context: Context) : LinearLayout(context) {

    private var textToShow: TextView = TextView(ContextThemeWrapper(context, R.style.DefaultText), null , 0)
    private var isExpanded = true
    private var lastClickTimeStamp = 0.toLong()

    companion object {
        const val TAG = "MainView"
    }

    init {
        val margin = 8
        val layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(margin)
        layoutParams.leftMargin = 0

        textToShow.layoutParams = layoutParams

        orientation = LinearLayout.VERTICAL

        addView(textToShow)

        textToShow.setOnClickListener {
            switchTextToShowPosition()

            if (isDoubleClick(System.currentTimeMillis() ,lastClickTimeStamp)) {
                Log.v(TAG, "Double Clicked")
            } else {
                Log.v(TAG, "Clicked")
            }
            lastClickTimeStamp = System.currentTimeMillis()
        }

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FirstNoteEvent) {
        textToShow.text = event.content

        if (!isExpanded) {
            switchTextToShowPosition()
        }
    }

    private fun isDoubleClick(new: Long, old: Long) = new - old < 300

    private fun switchTextToShowPosition() {
        val targetX = if (isExpanded) -0.8f else 0.0f
        isExpanded = !isExpanded

        ObjectAnimator.ofFloat(textToShow, "translationX", textToShow.width * targetX).apply {
            duration = 300
            start()
        }
    }

}