package com.aw.commons

import android.content.Context

object AndroidUIUtils {
    fun getDimen(context: Context, resource: Int) : Float {
        return context.resources.getDimension(resource)
    }

    fun intToColorHex(color: Int) = String.format("#%06X", 0xFFFFFF and color)

}