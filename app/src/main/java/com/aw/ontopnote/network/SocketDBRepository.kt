package com.aw.ontopnote.network

import com.aw.ontopnote.MainApp
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository

object SocketDBRepository {
    fun createNote(note: Note, callback: (createdNote: Note) -> Unit) {
        SocketServiceRepository.createNoteAndUpdateDB(note, SocketManager.socket) {
            //ensure it's on DB first
            var noteDB = NoteRepository.getNoteById(MainApp.applicationContext(), note.id)

            @Suppress("SENSELESS_COMPARISON")
            if (noteDB == null) {
                noteDB = NoteRepository.insertNote(MainApp.applicationContext(), it)
            }
            NoteRepository.updateNote(MainApp.applicationContext(), noteDB)
            callback(noteDB)
        }
    }
}