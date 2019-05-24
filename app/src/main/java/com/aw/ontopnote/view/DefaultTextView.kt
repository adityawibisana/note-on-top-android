package com.aw.ontopnote.view

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.R
import com.aw.ontopnote.helper.SingletonHolder
import com.aw.ontopnote.helper.Utils
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository

class DefaultTextView private constructor(context: Context) {

    private val inflater:LayoutInflater by lazy {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
    }

    init {

    }

    companion object : SingletonHolder<DefaultTextView, Context>(::DefaultTextView)

    fun generateTextView(note: Note): TextView {
        var textView = inflater.inflate(R.layout.view_default_text_view, null) as TextView
        textView.tag = note
        textView.text = note.content

        textView.setOnClickListener {

            //ensure we get the latest note pointer
            val latestNote = NoteRepository.getNoteById(MainApp.applicationContext(), note.id)

            latestNote.isHidden = !latestNote.isHidden
            NoteRepository.updateNote(MainApp.applicationContext(), latestNote)
        }

        return textView
    }

    fun decorateTextView (textView: TextView, note: Note) : TextView {
        textView.visibility = if (note.content.isBlank() && !note.isHidden) View.GONE else View.VISIBLE

        textView.text = if (note.isHidden) "" else note.content

        DrawableCompat.setTint(
            textView.background,
            Utils.rgbToColorRes(MainApp.applicationContext(), note.color)
        )

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, note.fontSize.toFloat())

        return textView
    }
}