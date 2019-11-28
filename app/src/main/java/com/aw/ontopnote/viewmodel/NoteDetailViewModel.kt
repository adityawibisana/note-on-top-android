package com.aw.ontopnote.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.model.event.SocketUpdateNoteEvent
import com.aw.ontopnote.network.SocketDBRepository
import com.aw.ontopnote.network.SocketDataSource
import com.aw.ontopnote.network.SocketManager
import com.aw.ontopnote.util.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.coroutines.CoroutineContext

class NoteDetailViewModel : BaseViewModel() {
    lateinit var note: Note

    var noteId: String = ""

    val mLiveNote: MutableLiveData<Note> by lazy {
        MutableLiveData<Note>()
    }

    val liveNote: LiveData<Note>
    get () = mLiveNote

    fun initialize(noteId: String) {
        this.noteId = noteId
        launch (Default) {
            note = NoteRepository.getNoteById(MainApp.applicationContext(), noteId)

            if (SharedPref.token != null) {
                if (SocketManager.connect()) {
                    if (note.remoteId == "") {
                        note = SocketDBRepository.createNote(note)
                    }
                }
            }
            if (!EventBus.getDefault().isRegistered(this@NoteDetailViewModel)) {
                EventBus.getDefault().register(this@NoteDetailViewModel)
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

    fun uploadNote() {
        launch (Default) {
            if (SharedPref.token != null) {
                if (note.remoteId == "") {
                    note = SocketDBRepository.createNote(note)
                }
                SocketDataSource.updateNote(note, SocketManager.socket)
            }
        }
    }

    fun getNoteValue() : Note {
        return NoteRepository.getNoteById(MainApp.applicationContext(), noteId)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onSocketUpdateNoteEvent(event: SocketUpdateNoteEvent) {
        if (event.note.id != noteId) {
            return
        }

        mLiveNote.postValue(event.note)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }
}
