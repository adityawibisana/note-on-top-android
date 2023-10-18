package com.aw.ontopnote.usecase

import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteDao

class GetLastEditedNoteImp(private val noteDao: NoteDao) : GetLastEditedNote {
    override fun invoke() : Note {
        if (noteDao.count() == 0) {
            noteDao.insertNote(Note(text = "Do awesome thing"))
        }
        return noteDao.getLastEditedNote()!!
    }
}