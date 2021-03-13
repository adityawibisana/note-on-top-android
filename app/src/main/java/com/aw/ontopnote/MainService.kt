package com.aw.ontopnote

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import com.aw.commons.ScopeUtils
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.view.ViewManager
import kotlinx.coroutines.launch

class MainService : Service() {
    lateinit var viewManager: ViewManager

    override fun onCreate() {
        super.onCreate()

        ScopeUtils.db.launch {
            val lastEditedNote = NoteRepository.getLastEditedNoteLive(MainApp.applicationContext())
            ScopeUtils.ui.launch {
                viewManager = ViewManager(this@MainService, getSystemService(Context.WINDOW_SERVICE) as WindowManager, lastEditedNote)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewManager.removeAll()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}