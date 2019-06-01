package com.aw.ontopnote

import CommonUtils
import CommonUtils.runOnDefaultThread
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import kotlinx.android.synthetic.main.default_action_bar.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 1
        const val TAG = "MainActivity"
    }

    lateinit var firstNote: Note
    lateinit var firstNoteLive: LiveData<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDrawOverlayPermission()

        runOnDefaultThread({
            firstNote = NoteRepository.getOrCreateFirstNote(applicationContext)

            firstNoteLive = NoteRepository.getLiveDataNoteById(applicationContext, firstNote.id)

            runOnUiThread {
                firstNoteLive.observe(this, Observer<Note> {
                    Log.v(TAG, "First note is changed, value: ${it.content}")
                })
            }
        })

        /** Temporarily hide feature to change custom note's padding size
        supportActionBar?.displayOptions = DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.default_action_bar)
        ic_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java) 
            startActivity(intent)
        }
        **/
    }

    private fun checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(Intent(applicationContext, MainService::class.java))
        } else if (Settings.canDrawOverlays(this)) {
            onActivityResult(REQUEST_CODE, -1, null)
        } else {
            val uri = Uri.parse("package:$packageName")
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri), REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            startService(Intent(applicationContext, MainService::class.java))
        }
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
