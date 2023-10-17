package com.aw.ontopnote

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.aw.ontopnote.helper.Utils
import com.aw.ontopnote.util.SharedPref
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    var isPaused = false

    companion object {
        const val REQUEST_CODE = 1
    }

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
            startService(Intent(applicationContext, MainService::class.java))
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
                startService(Intent(applicationContext, MainService::class.java))
            }
        }
    }

    private fun showPermissionOrProceedToApp() {
        lifecycleScope.launch (Default) {
            if (Utils.canDrawOverlays(this@BaseActivity)) {
                startService(Intent(applicationContext, MainService::class.java))
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