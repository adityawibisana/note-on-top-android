package com.aw.ontopnote

import CommonUtils.runOnDefaultThread
import CommonUtils.runOnUIThread
import android.content.Context
import android.widget.*
import com.aw.ontopnote.model.event.FirstNoteEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import android.view.LayoutInflater
import android.view.View
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.model.event.SwitchEditModeEvent
import kotlinx.android.synthetic.main.view_main.view.*


class MainView(context: Context) : RelativeLayout(context) {

    companion object {
        const val TAG = "MainView"
    }

    init {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.view_main, this)

        text.setOnClickListener {
            EventBus.getDefault().post(SwitchEditModeEvent(true))
        }

        runOnDefaultThread({
            val firstNote = NoteRepository.getOrCreateFirstNote(context.applicationContext)
            runOnUIThread( {
                text.text = firstNote.content
            })
        })

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: FirstNoteEvent) {
        text.text = event.content
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SwitchEditModeEvent) {
        if (event.isEditable) View.GONE else View.VISIBLE
    }
}