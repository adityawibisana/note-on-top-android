package com.aw.ontopnote

import android.content.Context
import android.view.ContextThemeWrapper
import android.widget.*
import androidx.core.view.setMargins
import com.aw.ontopnote.model.event.FirstNoteEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe

class MainView(context: Context) : LinearLayout(context) {

    private var textToShow: TextView = TextView(ContextThemeWrapper(context, R.style.DefaultText), null , 0)

    companion object {
        const val TAG = "MainView"
    }

    init {
        val margin = 8
        val layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(margin)
        layoutParams.leftMargin = 0

        textToShow.layoutParams = layoutParams
        textToShow.text = "Beli tiket Avengers Endgame"

        orientation = LinearLayout.VERTICAL

        addView(textToShow)

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FirstNoteEvent) {
        textToShow.text = event.content
    }

}