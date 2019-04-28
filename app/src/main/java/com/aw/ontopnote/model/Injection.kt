package com.aw.ontopnote.model

import android.content.Context

object Injection {
    fun provideNoteDataSource(context: Context) : NoteDao {
        return NotesDatabase.getInstance(context).noteDao()
    }
}