package com.aw.ontopnote

import CommonUtils.runOnDefaultThread
import CommonUtils.runOnUIThread
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.aw.ontopnote.model.event.FirstNoteEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import android.util.Log
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.view_main_editable.view.*
import android.view.View
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.model.event.SwitchEditModeEvent


class MainViewEditable (context: Context) : RelativeLayout(context) {

    companion object {
        const val TAG = "MainViewEditable"
    }

    private lateinit var firstNote: Note

    init {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.view_main_editable, this)

        root.setOnClickListener {
            Log.v(TAG, "Clicked")
            EventBus.getDefault().post(SwitchEditModeEvent(false))
        }

        runOnDefaultThread({
            firstNote = NoteRepository.getOrCreateFirstNote(context.applicationContext)
            runOnUIThread( {
                text_to_show.setText(firstNote.content)
            })
        })

        text_to_show.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                runOnDefaultThread({
                    if (::firstNote.isInitialized) {
                        firstNote.content = s.toString()
                        NoteRepository.updateNote(context.applicationContext, firstNote)

                        EventBus.getDefault().post(FirstNoteEvent(firstNote.content))
                    }
                })
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SwitchEditModeEvent) {
        Log.v(TAG, "visibility changed to:${event.isEditable}")

        when (event.isEditable) {
            true -> {
                visibility = View.VISIBLE

                CommonUtils.forceShowKeyboard(context, text_to_show)
                CommonUtils.runOnUIThread({
                    text_to_show.setSelection(text_to_show.text.length)
                }, 80)
            }
            false -> {
                visibility = View.GONE
                CommonUtils.hideKeyboard(context, text_to_show)
            }
        }
    }
}