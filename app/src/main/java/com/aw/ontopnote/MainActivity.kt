package com.aw.ontopnote

import CommonUtils.runOnDefaultThread
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import android.util.Log

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
