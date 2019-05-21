package com.aw.ontopnote.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.R
import com.aw.ontopnote.helper.SingletonHolder
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


}