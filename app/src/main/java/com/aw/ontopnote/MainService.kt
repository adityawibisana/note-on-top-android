package com.aw.ontopnote

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.aw.ontopnote.event.LastEditedNoteChanged
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.view.ViewManager
import org.greenrobot.eventbus.EventBus

class MainService : Service() {

    lateinit var lastEditedNoteObserver: Observer<Note>

    lateinit var viewManager: ViewManager

    override fun onCreate() {
        super.onCreate()

        viewManager = ViewManager(this, getSystemService( Context.WINDOW_SERVICE) as WindowManager)

        updateObserver()
    }

    private fun updateObserver() {
        val lastNoteLive = NoteRepository.getLastEditedNoteLive(MainApp.applicationContext())
        lastEditedNoteObserver = Observer { note ->
            note ?: return@Observer
            EventBus.getDefault().post(LastEditedNoteChanged(note))
        }
        lastNoteLive?.observeForever(lastEditedNoteObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}