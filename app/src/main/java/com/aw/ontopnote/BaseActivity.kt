package com.aw.ontopnote

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.aw.ontopnote.helper.Utils
import com.aw.ontopnote.util.SharedPref
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), CoroutineScope {
    var isPaused = false

    companion object {
        const val REQUEST_CODE = 1
    }

    private val job: Job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        isPaused = false

        if (SharedPref.isFirstTimeOpenApp) {
            SharedPref.isFirstTimeOpenApp = false
            showPermissionOrProceedToApp()
        } else {
            ContextCompat.startForegroundService(applicationContext, Intent(applicationContext, MainService::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        isPaused = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (!Utils.canDrawOverlays(this)) {
                ProcessPhoenix.triggerRebirth(applicationContext, Intent(applicationContext, MainActivity::class.java))
            } else {
                ContextCompat.startForegroundService(applicationContext, Intent(applicationContext, MainService::class.java))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun showPermissionOrProceedToApp() {
        launch (Default) {
            if (Utils.canDrawOverlays(this@BaseActivity)) {
                ContextCompat.startForegroundService(applicationContext, Intent(applicationContext, MainService::class.java))
            } else {
                launch (Main) {
                    Toast.makeText(this@BaseActivity, R.string.allow_draw_over_other_app_permission, Toast.LENGTH_LONG).show()
                    val uri = Uri.parse("package:$packageName")
                    startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri), REQUEST_CODE)
                }
            }
        }
    }
}