package com.aw.ontopnote

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.aw.ontopnote.screen.ExitAppScreen
import com.aw.ontopnote.screen.OverlayPermissionScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var drawOverlayServiceIntent: Intent
    var drawService: MainService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        drawOverlayServiceIntent = Intent(applicationContext, MainService::class.java)
        setContent {
            RootViewApp()
        }
    }

    @SuppressLint("NewApi")
    @Composable
    fun RootViewApp() {
        var canDrawOverlay by remember { mutableStateOf(canDrawOverlay()) }

        if (canDrawOverlay) {
            startService(drawOverlayServiceIntent)
            bindService(drawOverlayServiceIntent, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val binder = service as MainService.LocalBinder
                    drawService = binder.service
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    drawService = null
                }

            }, BIND_NOT_FOREGROUND)
            ExitAppScreen {
                drawService?.stopSelf()
                finish()
            }
        } else {
            OverlayPermissionScreen(
                goToOverlayPermission = rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult()) { _ ->
                    canDrawOverlay = canDrawOverlay()
                },
                overlayPermissionIntent = getPermissionOverlayIntent()
            )
        }
    }

    private fun getPermissionOverlayIntent() : Intent {
        val uri = Uri.parse("package:$packageName")
        return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
    }

    private fun canDrawOverlay(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    override fun onDestroy() {
        drawService = null
        super.onDestroy()
    }
}
