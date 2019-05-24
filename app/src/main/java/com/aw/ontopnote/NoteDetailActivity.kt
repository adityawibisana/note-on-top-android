package com.aw.ontopnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.view.DefaultTextView
import kotlinx.android.synthetic.main.activity_note_detail.*

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extraNoteId"
    }

    private val defaultTextView: DefaultTextView by lazy {
        DefaultTextView.getInstance(this)
    }

    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        CommonUtils.runOnDefaultThread({
            note = NoteRepository.getNoteById(MainApp.applicationContext(), intent.getStringExtra(EXTRA_NOTE_ID))
            val noteTextView = tv_note as TextView
            defaultTextView.decorateTextView(noteTextView, note)
        })
    }
}
