package com.aw.ontopnote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.network.SocketManager
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


    val note: LiveData<Note> by lazy {
        NoteRepository.getLiveDataNoteById(MainApp.applicationContext(), noteId)
    }

    var noteId: String = ""
    set(value) {
        if (value != "") {
            field = value
            launch (Default) { note }
        }
    }

    fun updateNote(fontSize: Int? = null, viewType: Int? = null, color: Int? = null, text: String? = null) {
        launch (Default) {
            val noteDb = note.value
            if (noteDb != null) {
                if (fontSize != null) {
                    noteDb.fontSize = fontSize
                }
                if (viewType != null) {
                    noteDb.viewType = viewType
                }
                if (color != null) {
                    noteDb.color = color
                }
                if (text != null) {
                    noteDb.text = text
                }

                NoteRepository.updateNote(MainApp.applicationContext(), noteDb)
            }
        }
    }

    suspend fun getNoteValue() : Note {
        return NoteRepository.getNoteById(MainApp.applicationContext(), noteId)
    }

    fun uploadNote() {
        launch (Default) {
            if (note.value != null) {
                SocketManager.updateNote(note.value!!)
            }
        }
    }
}
