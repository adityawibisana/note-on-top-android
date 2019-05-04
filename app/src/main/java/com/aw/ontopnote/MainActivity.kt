package com.aw.ontopnote

import CommonUtils.runOnDefaultThread
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import com.aw.ontopnote.model.Note
import kotlinx.android.synthetic.main.activity_main.*
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.model.event.FirstNoteEvent
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 1
        const val TAG = "MainActivity"
    }

    lateinit var firstNote: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDrawOverlayPermission()

        runOnDefaultThread({
            firstNote = NoteRepository.getOrCreateFirstNote(applicationContext)
            runOnUiThread {
                etNote.setText(firstNote.content)
            }
        })

        etNote.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                runOnDefaultThread({
                    if (::firstNote.isInitialized) {
                        firstNote.content = s.toString()
                        NoteRepository.updateNote(applicationContext, firstNote)

                        EventBus.getDefault().post(FirstNoteEvent(firstNote.content))
                    }
                })
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
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
}
