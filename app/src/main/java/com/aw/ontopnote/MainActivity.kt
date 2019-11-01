package com.aw.ontopnote

import CommonUtils
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val firstNote: Note by lazy {
        NoteRepository.getOrCreateFirstNote(applicationContext)
    }

    private val firstNoteLive: LiveData<Note> by lazy {
        NoteRepository.getLiveDataNoteById(applicationContext, firstNote.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch (Dispatchers.Default) {
            if (NoteRepository.getNoteCount(this@MainActivity) <= 1) {   
                launch (Dispatchers.Main) {
                    firstNoteLive.observe(this@MainActivity, Observer<Note> {
                        Log.v(TAG, "First note is changed, value: ${it.text}")
                    })
                }
                goToNoteDetail(null)
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

    fun addNote(v: View) {
        CommonUtils.runOnDefaultThread({
            NoteRepository.insertNote(applicationContext, Note(text = "2nd note"))
        })
    }

    fun goToNoteDetail(v: View?) {
        val intent = Intent(this, NoteDetailActivity::class.java)
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, firstNote.id)
        startActivity(intent)
    }
}
