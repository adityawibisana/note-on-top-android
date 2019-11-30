package com.aw.ontopnote.network

import com.aw.ontopnote.MainApp
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.model.event.SocketUpdateNoteEvent
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object SocketDBRepository {
    suspend fun createNote(note: Note): Note = suspendCoroutine { continuation ->
        val scope = CoroutineScope(continuation.context)
        scope.launch {
            SocketDataSource.createNote(note, SocketManager.socket)
            //ensure it's on DB first
            @Suppress("SENSELESS_COMPARISON")
            if (NoteRepository.getNoteById(MainApp.applicationContext(), note.id) == null) {
                NoteRepository.insertNote(MainApp.applicationContext(), note)
            }
            NoteRepository.updateNote(MainApp.applicationContext(), note)
            continuation.resume(note)
        }
    }

    fun handleUpdatedNote(res: Array<Any>) {
        try {
            val json = JSONObject(res[0].toString())
            val parsedNote = Gson().fromJson(json.getString("message"), Note::class.java) ?: return
            NoteRepository.updateNote(MainApp.applicationContext(), parsedNote)
            EventBus.getDefault().post(SocketUpdateNoteEvent(parsedNote))
        } catch (ignored: Exception) { }
    }

    suspend fun getLastEditedNote(): Note? = suspendCoroutine { continuation ->
        val scope = CoroutineScope(continuation.context)
        scope.launch {
            val note = SocketDataSource.getLastEditedNote(SocketManager.socket)
            if (note == null) {
                continuation.resume(null)
                return@launch
            }
            //ensure it's on DB first
            @Suppress("SENSELESS_COMPARISON")
            if (NoteRepository.getNoteByRemoteId(MainApp.applicationContext(), note.remoteId) == null) {
                NoteRepository.insertNote(MainApp.applicationContext(), note)
            }
            NoteRepository.updateNote(MainApp.applicationContext(), note)
            continuation.resume(note)
        }
    }
}