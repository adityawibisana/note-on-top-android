package com.aw.ontopnote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.network.SocketDBRepository
import com.aw.ontopnote.network.SocketManager
import com.aw.ontopnote.util.SharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch (Default) {
            if (SharedPref.token != null) {
                SocketManager.connect()
                val note = SocketDBRepository.getLastEditedNote()
                if (note != null) {
                    goToNoteDetail(note.id)
                    finishAffinity()
                    return@launch
                }
            }

            var lastEditedNote = NoteRepository.getLastEditedNote(applicationContext)
            if (lastEditedNote == null) {
                lastEditedNote = NoteRepository.getOrCreateFirstNote(applicationContext)
            }
            goToNoteDetail(lastEditedNote.id)
            finishAffinity()
        }

        /** Temporarily hide feature to change custom note's padding size
        supportActionBar?.displayOptions = DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.default_action_bar)
        ic_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java) 
            startActivity(intent)
        }
        **/
    }

    fun goToNoteDetail(noteId: String) {
        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, noteId)
        startActivity(intent)
    }
}
