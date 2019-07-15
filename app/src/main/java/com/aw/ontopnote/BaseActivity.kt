package com.aw.ontopnote

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aw.ontopnote.helper.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), CoroutineScope {
    companion object {
        const val REQUEST_CODE = 1
    }

    private val job: Job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onResume() {
        super.onResume()
        showPermissionOrProceedToApp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            showPermissionOrProceedToApp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun showPermissionOrProceedToApp() {
        launch (Default) {
            if (Utils.canDrawOverlays(this@BaseActivity)) {
                startService(Intent(applicationContext, MainService::class.java))
            } else {
                val uri = Uri.parse("package:$packageName")
                startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri), REQUEST_CODE)
                Toast.makeText(this@BaseActivity, R.string.allow_draw_over_other_app_permission, Toast.LENGTH_LONG).show()
            }
        }
    }
}