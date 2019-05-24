package com.aw.ontopnote

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import kotlinx.android.synthetic.main.activity_note_detail.*

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extraNoteId"
    }

    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        CommonUtils.runOnDefaultThread({
            note = NoteRepository.getNoteById(MainApp.applicationContext(), intent.getStringExtra(EXTRA_NOTE_ID))
            val noteTextView = tv_note as TextView
            noteTextView.text = note.content
        })
    }
}
