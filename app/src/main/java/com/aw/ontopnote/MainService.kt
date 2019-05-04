package com.aw.ontopnote

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.aw.ontopnote.model.event.SwitchEditModeEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainService : Service() {

    private val mViewEditable: MainViewEditable by lazy {
        MainViewEditable(applicationContext)
    }

    private val mView: MainView by lazy {
        MainView(applicationContext)
    }

    private val mWindowManager: WindowManager by lazy {
        (getSystemService( Context.WINDOW_SERVICE) as WindowManager)
    }

    private val layoutFlag: Int by lazy {
        if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE
    }

    private val editModeLayoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
            PixelFormat.TRANSLUCENT
        )
    }

    private val viewModeLayoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()

        editModeLayoutParams.gravity = Gravity.TOP or Gravity.START
        viewModeLayoutParams.gravity = Gravity.TOP or Gravity.START

        mWindowManager.addView(mViewEditable, editModeLayoutParams)
        mWindowManager.addView(mView, viewModeLayoutParams)

        mView.visibility = View.GONE

        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: SwitchEditModeEvent) {
        mViewEditable.visibility = if (event.isEditable) View.VISIBLE else View.GONE
        mView.visibility = if (event.isEditable) View.GONE else View.VISIBLE
    }
}