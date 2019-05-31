package com.aw.ontopnote.model

import CommonUtils
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.aw.ontopnote.R
import com.aw.ontopnote.model.event.UpdateNoteEvent
import org.greenrobot.eventbus.EventBus

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

    fun getNoteById(context: Context, id: String) : Note {
        val noteDao = NotesDatabase.getInstance(context).noteDao()
        return noteDao.getNoteById(id)
    }

    fun getLiveDataNoteById(context: Context, id: String) : LiveData<Note> {
        val noteDao = NotesDatabase.getInstance(context).noteDao()
        return noteDao.getLiveDataNoteById(id)
    }

    fun updateNote(context: Context, note: Note) {
        CommonUtils.runOnDefaultThread({
            val noteDao = NotesDatabase.getInstance(context).noteDao()
            noteDao.update(note)

            Log.v(TAG, "updated note with id:" + note.id)
            EventBus.getDefault().post(UpdateNoteEvent(note))
        })
    }

    fun getAllNotes(context: Context): List<Note>  {
        val noteDao = NotesDatabase.getInstance(context).noteDao()
        return noteDao.getAllNotesList()
    }
}