package com.aw.ontopnote.helper

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.aw.ontopnote.R

object Utils {

    fun rgbToColorRes(context: Context, rgbColor: Int) : Int {
        return when (rgbColor) {
            ContextCompat.getColor(context, R.color.redMaterial) -> ContextCompat.getColor(context, R.color.redMaterial)
            ContextCompat.getColor(context, R.color.pinkMaterial) -> ContextCompat.getColor(context, R.color.pinkMaterial)
            ContextCompat.getColor(context, R.color.purpleMaterial) -> ContextCompat.getColor(context, R.color.purpleMaterial)
            ContextCompat.getColor(context, R.color.deepPurpleMaterial) -> ContextCompat.getColor(context, R.color.deepPurpleMaterial)
            ContextCompat.getColor(context, R.color.indigoMaterial) -> ContextCompat.getColor(context, R.color.indigoMaterial)
            ContextCompat.getColor(context, R.color.lightBluetMaterial) -> ContextCompat.getColor(context, R.color.lightBluetMaterial)
            ContextCompat.getColor(context, R.color.cyanMaterial) -> ContextCompat.getColor(context, R.color.cyanMaterial)
            ContextCompat.getColor(context, R.color.tealMaterial) -> ContextCompat.getColor(context, R.color.tealMaterial)
            ContextCompat.getColor(context, R.color.greenMaterial) -> ContextCompat.getColor(context, R.color.greenMaterial)
            ContextCompat.getColor(context, R.color.lightGreenMaterial) -> ContextCompat.getColor(context, R.color.lightGreenMaterial)
            ContextCompat.getColor(context, R.color.limeMaterial) -> ContextCompat.getColor(context, R.color.limeMaterial)
            ContextCompat.getColor(context, R.color.yellowMaterial) -> ContextCompat.getColor(context, R.color.yellowMaterial)
            ContextCompat.getColor(context, R.color.amberMaterial) -> ContextCompat.getColor(context, R.color.amberMaterial)
            ContextCompat.getColor(context, R.color.orangeMaterial) -> ContextCompat.getColor(context, R.color.orangeMaterial)
            ContextCompat.getColor(context, R.color.deepOrangeMaterial) -> ContextCompat.getColor(context, R.color.deepOrangeMaterial)
            ContextCompat.getColor(context, R.color.brownMaterial) -> ContextCompat.getColor(context, R.color.brownMaterial)
            ContextCompat.getColor(context, R.color.greyMaterial) -> ContextCompat.getColor(context, R.color.greyMaterial)
            ContextCompat.getColor(context, R.color.blueGreyMaterial) -> ContextCompat.getColor(context, R.color.blueGreyMaterial)
            ContextCompat.getColor(context, R.color.black) -> ContextCompat.getColor(context, R.color.black)
            else -> {
                ContextCompat.getColor(context, R.color.blueMaterial)
            }
        }
    }

    /**
     * Workaround for Android O that always return true when checking via Settings.canDrawOverlays
     */
    fun canDrawOverlays(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true
        else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            return Settings.canDrawOverlays(context)
        } else {
            if (Settings.canDrawOverlays(context)) return true
            try {
                val mgr = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val viewToAdd = View(context)
                val params = WindowManager.LayoutParams(
                    0,
                    0,
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                    else
                        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT
                )
                viewToAdd.layoutParams = params
                mgr.addView(viewToAdd, params)
                mgr.removeView(viewToAdd)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }
    }
}