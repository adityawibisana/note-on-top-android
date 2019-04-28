package com.aw.ontopnote.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.aw.ontopnote.R

object NoteRepository {
    fun insertNote(context: Context, note: Note) {
        CommonUtils.runOnDefaultThread({
            NotesDatabase.getInstance(context).noteDao().insertNote(note)
        })
    }

    fun getOrCreateFirstNote(context: Context) : LiveData<Note> {
        val noteDao = NotesDatabase.getInstance(context).noteDao()

        if (noteDao.count() == 0) {
            noteDao.insertNote(Note(content = context.getString(R.string.input_note_here)))
        }
        return noteDao.getFirstNote()
    }

    fun getNoteById(context: Context, id: String) : LiveData<Note> {
        val noteDao = NotesDatabase.getInstance(context).noteDao()
        return noteDao.getNoteById(id)
    }

    fun updateNote(context: Context, note: Note) {
        CommonUtils.runOnDefaultThread({
            val noteDao = NotesDatabase.getInstance(context).noteDao()
            noteDao.update(note)
        })
    }
}