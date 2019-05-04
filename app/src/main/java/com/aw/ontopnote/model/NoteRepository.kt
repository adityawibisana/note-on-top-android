package com.aw.ontopnote.model

import android.content.Context
import androidx.lifecycle.LiveData
import com.aw.ontopnote.R

import android.util.Log

object NoteRepository {
    private const val TAG = "NoteRepository"

    fun insertNote(context: Context, note: Note) {
        CommonUtils.runOnDefaultThread({
            NotesDatabase.getInstance(context).noteDao().insertNote(note)
        })
    }

    fun getOrCreateFirstNote(context: Context) : Note {
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

            Log.v(TAG, "updated")
        })
    }

    fun getAllNotes(context: Context): List<Note>  {
        val noteDao = NotesDatabase.getInstance(context).noteDao()
        return noteDao.getAllNotesList()
    }
}