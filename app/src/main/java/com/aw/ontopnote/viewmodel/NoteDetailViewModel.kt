package com.aw.ontopnote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.network.SocketDBRepository
import com.aw.ontopnote.network.SocketManager
import com.aw.ontopnote.util.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NoteDetailViewModel : ViewModel(), CoroutineScope {
    private val job: Job by lazy { Job() }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    lateinit var note: Note

    var noteId: String = ""

    fun initialize(noteId: String) {
        this.noteId = noteId
        launch (Default) {
            note = NoteRepository.getNoteById(MainApp.applicationContext(), noteId)

            if (SharedPref.token != null) {
                SocketManager.connect()
            }

            if (note.remoteId == "") {
                val createdNote = SocketDBRepository.createNote(note)
                note.remoteId = createdNote.remoteId
            }
        }
    }

    fun updateNote(fontSize: Int? = null, viewType: Int? = null, color: Int? = null, text: String? = null) {
        launch (Default) {
            if (::note.isInitialized) {
                if (fontSize != null) {
                    note.fontSize = fontSize
                }
                if (viewType != null) {
                    note.viewType = viewType
                }
                if (color != null) {
                    note.color = color
                }
                if (text != null) {
                    note.text = text
                }

                NoteRepository.updateNote(MainApp.applicationContext(), note)
            }
        }
    }

    fun getNoteValue() : Note {
        return NoteRepository.getNoteById(MainApp.applicationContext(), noteId)
    }
}
