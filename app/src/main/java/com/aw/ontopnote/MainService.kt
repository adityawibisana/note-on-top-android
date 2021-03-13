package com.aw.ontopnote

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.aw.commons.ScopeUtils
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.view.ViewManager
import kotlinx.coroutines.launch

const val ID = 1
const val channelId = "OnTopNoteChannelId"
const val channelName = "OnTopNoteChannelName"
class MainService : Service() {
    lateinit var viewManager: ViewManager


    override fun onCreate() {
        super.onCreate()
        startForeground(ID, getNotification())

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

    private fun getNotification(): Notification {
        val channelId = createNotificationChannel(channelId, channelName)

        return NotificationCompat.Builder(this, channelId)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_DEFAULT)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        chan.setShowBadge(false)
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}