package com.aw.ontopnote

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.view.WindowManager
import com.aw.commons.ScopeUtils
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.view.ViewManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainService : Service() {
    private lateinit var binder: IBinder
    lateinit var viewManager: ViewManager

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder() {
        val service: MainService
            get() = this@MainService
    }

    override fun onCreate() {
        super.onCreate()
        binder = LocalBinder()
//        ScopeUtils.db.launch {
//            val lastEditedNote = NoteRepository.getLastEditedNoteLive(MainApp.applicationContext())
//            ScopeUtils.ui.launch {
//                viewManager = ViewManager(this@MainService, getSystemService(Context.WINDOW_SERVICE) as WindowManager, lastEditedNote)
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        viewManager.removeAll()
    }
}