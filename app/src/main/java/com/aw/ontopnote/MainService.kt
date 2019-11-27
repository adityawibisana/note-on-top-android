package com.aw.ontopnote

import CommonUtils.defaultScope
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.view.DefaultTextView
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainService : Service(), CoroutineScope {
    private val job: Job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = job + Main

    private val mWindowManager: WindowManager by lazy {
        (getSystemService( Context.WINDOW_SERVICE) as WindowManager)
    }

    private val defaultTextView: DefaultTextView by lazy {
        DefaultTextView.getInstance(this)
    }

    private val mLayoutParams: WindowManager.LayoutParams  by lazy {
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT)
    }

    private val layoutFlag: Int by lazy {
        if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val textViews: ArrayList<TextView> by lazy {
        ArrayList<TextView>()
    }

    override fun onCreate() {
        super.onCreate()

        launch (Default) {
            Fabric.with(this@MainService, Crashlytics())
        }

        mLayoutParams.gravity = Gravity.START or Gravity.TOP

        val lastNoteLive = NoteRepository.getLastEditedNoteLive(MainApp.applicationContext())

        println("lastNoteLive is:$lastNoteLive")
        lastNoteLive?.observeForever { note ->
            note ?: return@observeForever

            if (textViews.size == 0) {
                val textView = defaultTextView.generateTextView(note)
                mWindowManager.addView(textView, mLayoutParams)
                textViews.add(textView)
            } else {
                textViews.find {
                    (it.tag as Note).id == note.id
                }?.let {
                    mWindowManager.updateViewLayout(
                        defaultTextView.decorateTextView(it, note),
                        mLayoutParams
                    )
                }
            }
        }
    }
}