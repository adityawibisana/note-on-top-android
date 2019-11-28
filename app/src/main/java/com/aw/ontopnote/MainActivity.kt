package com.aw.ontopnote

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.aw.ontopnote.viewmodel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private val model: MainActivityViewModel by lazy {
        ViewModelProviders.of(this@MainActivity)[MainActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launch (Default) {
            goToNoteDetail(model.getLastEditedNote()!!.id)
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
