package com.aw.ontopnote.network

import com.aw.ontopnote.MainApp
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object SocketDBRepository {
    suspend fun createNote(note: Note): Note = suspendCoroutine { continuation ->
        val scope = CoroutineScope(continuation.context)
        scope.launch {
            SocketRepository.createNote(note, SocketManager.socket)
            //ensure it's on DB first
            @Suppress("SENSELESS_COMPARISON")
            if (NoteRepository.getNoteById(MainApp.applicationContext(), note.id) == null) {
                NoteRepository.insertNote(MainApp.applicationContext(), note)
            }
            NoteRepository.updateNote(MainApp.applicationContext(), note)
            continuation.resume(note)
        }
    }
}