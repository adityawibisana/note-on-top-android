package com.aw.ontopnote

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.widget.*
import com.aw.ontopnote.model.event.FirstNoteEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import android.util.Log
import android.view.View
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.view_main.view.*


class MainView(context: Context) : RelativeLayout(context) {


    private var isExpanded = true
    private var lastClickTimeStamp = 0.toLong()

    companion object {
        const val TAG = "MainView"
    }

    init {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.view_main, this)

        text_to_show.setOnClickListener {
            switchTextToShowPosition()

            if (isDoubleClick(System.currentTimeMillis() ,lastClickTimeStamp)) {
                Log.v(TAG, "Double Clicked")

                val mainActivityIntent = Intent(context, MainActivity::class.java)
                mainActivityIntent.addCategory(Intent.CATEGORY_HOME)
                mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(mainActivityIntent)
            } else {
                Log.v(TAG, "Clicked")
            }
            lastClickTimeStamp = System.currentTimeMillis()
        }

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FirstNoteEvent) {
        text_to_show.text = event.content

        if (!isExpanded) {
            switchTextToShowPosition()
        }
    }

    private fun isDoubleClick(new: Long, old: Long) = new - old < 300

    private fun switchTextToShowPosition() {
        val targetX = if (isExpanded) -0.8f else 0.0f
        isExpanded = !isExpanded

        ObjectAnimator.ofFloat(text_to_show, "translationX", text_to_show.width * targetX).apply {
            duration = 300
            start()
        }
    }

}