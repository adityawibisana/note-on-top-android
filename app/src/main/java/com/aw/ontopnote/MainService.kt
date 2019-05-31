package com.aw.ontopnote

import CommonUtils
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
import com.aw.ontopnote.model.event.UpdateNoteEvent
import com.aw.ontopnote.view.DefaultTextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainService : Service() {

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

        mLayoutParams.gravity = Gravity.START or Gravity.TOP

        CommonUtils.runOnDefaultThread({
            var notes = NoteRepository.getAllNotes(MainApp.applicationContext())
            for (note in notes) {
                val textView = defaultTextView.generateTextView(note)
                mWindowManager.addView(textView, mLayoutParams)
                textViews.add(textView)
            }
        })

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UpdateNoteEvent) {
        CommonUtils.runOnDefaultThread({
            textViews.find {
                (it.tag as Note).id == event.note.id
            }?.let {
                // TODO: refactor it, move to default text view, using decorateTextView(textView, note) method

                mWindowManager.updateViewLayout(defaultTextView.decorateTextView(it, event.note), mLayoutParams)
            }
        })
    }
}