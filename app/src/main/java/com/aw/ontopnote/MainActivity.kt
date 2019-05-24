package com.aw.ontopnote

import CommonUtils.runOnDefaultThread
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import com.aw.ontopnote.model.Note
import kotlinx.android.synthetic.main.activity_main.*
import com.aw.ontopnote.model.NoteRepository
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import com.aw.ontopnote.helper.Utils
import kotlinx.android.synthetic.main.dialog_color.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 1
        const val TAG = "MainActivity"
    }

    lateinit var firstNote: Note

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(this)
            .setTitle(R.string.pick_color)
            .setView(R.layout.dialog_color)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private val textWatcher: TextWatcher by lazy {
        object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                runOnDefaultThread({
                    if (::firstNote.isInitialized) {
                        firstNote.content = s.toString()
                        firstNote.isHidden = false
                        NoteRepository.updateNote(applicationContext, firstNote)
                    }
                })
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDrawOverlayPermission()

        runOnDefaultThread({
            firstNote = NoteRepository.getOrCreateFirstNote(applicationContext)
            runOnUiThread {
                et_note.setText(firstNote.content)
            }
        })

        // TODO: update the value based on current note seek bar progress.
        sb_font_size.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (::firstNote.isInitialized) {
                    CommonUtils.runOnDefaultThread({
                        val convertedSize = progress / 100.0f * CommonUtils.getDimen(this@MainActivity, R.dimen.maximum_font_size)

                        firstNote.fontSize = convertedSize.toInt()
                        NoteRepository.updateNote(applicationContext, firstNote)
                    })
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })
    }

    override fun onResume() {
        super.onResume()
        et_note.addTextChangedListener(textWatcher)
    }

    override fun onPause() {
        super.onPause()
        et_note.removeTextChangedListener(textWatcher)
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

    fun showColorDialog(v: View) {
        if (!dialog.isShowing) {
            dialog.show()
        }

        CommonUtils.runOnDefaultThread({
            for (b in dialog.dialog_color_root.children) {
                if (b is Button) b.setOnClickListener {
                    val color = (it.background as ColorDrawable).color

                    firstNote.color = Utils.rgbToColorRes(this, color)

                    NoteRepository.updateNote(applicationContext, firstNote)
                }
            }
        })
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
