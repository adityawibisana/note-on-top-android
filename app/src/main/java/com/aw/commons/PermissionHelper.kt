package com.aw.commons

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import java.util.*

class PermissionHelper {
    companion object {
        val TAG: String = PermissionHelper::class.java.simpleName
        val PERMISSION_REQUEST_CODE = 1
    }

    private val requiredDangerousPermissions: ArrayList<String> by lazy {
        ArrayList<String>().apply {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.SEND_SMS)
            add(Manifest.permission.CALL_PHONE)
        }
    }

    fun askPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity, requiredDangerousPermissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }

    fun requestLocationPermission(activity: Activity) {
        askPermissions(activity, getLocationRelatedPermissionsList(activity))
    }

    /**
     * Check if there are something missing on the permission.
     */
    fun isAllPermissionGranted(activity: Activity) : Boolean {
        val unGrantedDangerousPermissions = getUngrantedDangerousPermissionsList(activity)

        val permissionSummary = getPermissionSummary(activity, unGrantedDangerousPermissions)

        if (permissionSummary == PermissionChecker.PERMISSION_GRANTED)
            return true

        return false
    }

    fun canShowPermissionRationaleDialog(activity: Activity, permissions: ArrayList<String>): Boolean {
        for (permission in permissions) {
            val shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            if (shouldShow) {
                return true
            }
        }
        return false // --> go to app settings
    }

    private fun askPermissions(activity: Activity, permissions: ArrayList<String>) {
        if (permissions.size > 0) {
            ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    private fun askPermission(activity: Activity, permission: String) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_REQUEST_CODE)
    }

    fun areAllRequiredPermissionsExcludingLocationPermissionsGranted(context: Context): Boolean {
        for (permission in requiredDangerousPermissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun isMicPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    fun isCameraPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun isSMSPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }

    fun isCallPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return false

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return false

        return true
    }

    private fun getUngrantedDangerousPermissionsList(activity: Activity): ArrayList<String> {
        val list = ArrayList<String>()
        for (permission in requiredDangerousPermissions) {
            val result = ActivityCompat.checkSelfPermission(activity, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                list.add(permission)
            }
        }
        return list
    }

    fun getLocationRelatedPermissionsList(activity: Activity): ArrayList<String> {
        val list = ArrayList<String>()
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        list.add(Manifest.permission.ACCESS_FINE_LOCATION)
        return list
    }

    private fun getPermissionSummary(activity: Activity, permissions: ArrayList<String>): Int {
        var returnValue = PermissionChecker.PERMISSION_GRANTED

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    return PermissionChecker.PERMISSION_DENIED_APP_OP
                }
                returnValue = PermissionChecker.PERMISSION_DENIED
            }
        }
        return returnValue
    }

    fun requestDrawOverlay(context: Context) {
        context.startActivity(Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + context.packageName)
        ))
    }

    fun canDrawOverlay(context: Context) : Boolean {
        val canDrawOverlays = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
        return canDrawOverlays
    }

    fun goToAppSetings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}
