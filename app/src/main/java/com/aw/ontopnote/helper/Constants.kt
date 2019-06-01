package com.aw.ontopnote.helper

import com.aw.ontopnote.MainApp
import com.aw.ontopnote.R

object Constants {
    const val MAXIMUM_NOTE_FONT_SIZE = 100

    const val MINIMUM_NOTE_PADDING_SIZE = 15

    val DEFAULT_NOTE_PADDING_SIZE: Int by lazy {
        MainApp.applicationContext().resources.getDimension(R.dimen.margin).toInt()
    }
}