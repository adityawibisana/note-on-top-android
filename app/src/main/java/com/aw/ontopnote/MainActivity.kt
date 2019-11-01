package com.aw.ontopnote

import CommonUtils
import CommonUtils.runOnDefaultThread
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.network.SocketManager
import kotlinx.android.synthetic.main.default_action_bar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var firstNote: Note
    private lateinit var firstNoteLive: LiveData<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch (Dispatchers.Default) {
            firstNote = NoteRepository.getOrCreateFirstNote(applicationContext)

            firstNoteLive = NoteRepository.getLiveDataNoteById(applicationContext, firstNote.id)

            launch (Dispatchers.Main) {
                firstNoteLive.observe(this@MainActivity, Observer<Note> {
                    Log.v(TAG, "First note is changed, value: ${it.content}")
                })
            }

            SocketManager.connect()
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

    fun addNote(v: View) {
        CommonUtils.runOnDefaultThread({
            NoteRepository.insertNote(applicationContext, Note(content = "2nd note"))
        })
    }

    fun goToNoteDetail(v: View) {
        if (::firstNote.isInitialized) {
            val intent = Intent(this, NoteDetailActivity::class.java)
            intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, firstNote.id)
            startActivity(intent)
        }
    }
}
