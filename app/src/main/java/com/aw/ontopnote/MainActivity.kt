package com.aw.ontopnote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
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

        launch (Default) {
            firstNote = NoteRepository.getOrCreateFirstNote(applicationContext)
            firstNoteLive = NoteRepository.getLiveDataNoteById(applicationContext, firstNote.id)
            if (NoteRepository.getNoteCount(this@MainActivity) <= 1) {   
                launch (Dispatchers.Main) {
                    firstNoteLive.observe(this@MainActivity, Observer<Note> {
                        Log.v(TAG, "First note is changed, value: ${it.text}")
                    })
                }
                goToNoteDetail(null)
                finishAffinity()
            }
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

    fun goToNoteDetail(v: View?) {
        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, firstNote.id)
        startActivity(intent)
    }
}
