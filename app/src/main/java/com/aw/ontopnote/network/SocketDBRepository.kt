package com.aw.ontopnote.network

import com.aw.ontopnote.MainApp
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository

object SocketDBRepository {
    fun createNote(note: Note, callback: (createdNote: Note) -> Unit) {
        SocketRepository.createNote(note, SocketManager.socket) {
            //ensure it's on DB first
            @Suppress("SENSELESS_COMPARISON")
            if (NoteRepository.getNoteById(MainApp.applicationContext(), note.id) == null) {
                NoteRepository.insertNote(MainApp.applicationContext(), it)
            }
            NoteRepository.updateNote(MainApp.applicationContext(), it)
            callback(it)
        }
    }
}