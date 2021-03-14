package com.aw.ontopnote.event

import android.view.View
import android.view.WindowManager

data class WindowManagerLayoutParamsChanged(val view: View, val layoutParams: WindowManager.LayoutParams)