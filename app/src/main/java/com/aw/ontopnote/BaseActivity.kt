package com.aw.ontopnote

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aw.ontopnote.helper.Utils

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE = 1
    }

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

    private fun showPermissionOrProceedToApp() {
        CommonUtils.runOnDefaultThread({
            if (Utils.canDrawOverlays(this)) {
                startService(Intent(applicationContext, MainService::class.java))
            } else {
                val uri = Uri.parse("package:$packageName")
                startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri), REQUEST_CODE)
                Toast.makeText(this, R.string.allow_draw_over_other_app_permission, Toast.LENGTH_LONG).show()
            }
        })
    }
}