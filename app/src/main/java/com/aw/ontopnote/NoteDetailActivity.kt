package com.aw.ontopnote

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aw.ontopnote.model.ViewType
import com.aw.ontopnote.viewmodel.NoteDetailViewModel
import kotlinx.android.synthetic.main.activity_note_detail.*
import kotlinx.android.synthetic.main.dialog_color.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class NoteDetailActivity : BaseActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extraNoteId"
        const val TAG = "NoteDetailActivity"
    }

    private val model: NoteDetailViewModel by lazy {
        ViewModelProviders.of(this@NoteDetailActivity)[NoteDetailViewModel::class.java]
    }

    var pauseTextWatcher = false

    private val textWatcher: TextWatcher by lazy {
        object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//                Log.v(TAG, "Note changed to:${s.toString()}")
                if (!pauseTextWatcher) {
                    model.updateNote(text = s.toString())
                    model.uploadNote()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        }
    }

    private val seekBarFontSizeChangeListener: SeekBar.OnSeekBarChangeListener by lazy {
        object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                model.updateNote(fontSize = progress)
                model.uploadNote()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        }
    }

    private val onStickyNoteChangedListener: CompoundButton.OnCheckedChangeListener by lazy {
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            val viewType = if (isChecked) ViewType.VISIBLE else ViewType.GONE
            model.updateNote(viewType = viewType)
            model.uploadNote()
        }
    }

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(this)
            .setTitle(R.string.pick_color)
            .setView(R.layout.dialog_color)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val noteId = intent.getStringExtra(EXTRA_NOTE_ID)
        model.initialize(noteId)
        model.liveNote.observe(this, Observer {
            Log.v(TAG, "Live note is updated")
            pauseTextWatcher = true
            et_note.setText(it.text)
            pauseTextWatcher = false

        })
    }

    override fun onResume() {
        super.onResume()
        launch (Default) {
            val note = model.getNoteValue()
            launch (Main) {
                et_note.setText(note.text)
                sb_font_size.progress = note.fontSize
                tb_always_show.isChecked = note.viewType != ViewType.GONE
            }
            et_note.addTextChangedListener(textWatcher)
            sb_font_size.setOnSeekBarChangeListener(seekBarFontSizeChangeListener)
            tb_always_show.setOnCheckedChangeListener(onStickyNoteChangedListener)
        }
    }

    override fun onPause() {
        super.onPause()
        et_note.removeTextChangedListener(textWatcher)
        sb_font_size.setOnSeekBarChangeListener(null)
        tb_always_show.setOnCheckedChangeListener(null)
    }

    fun showColorDialog(v: View) {
        if (!dialog.isShowing) {
            dialog.show()
        }

        for (b in dialog.dialog_color_root.children) {
            if (b is Button) b.setOnClickListener {
                val color = (it.background as ColorDrawable).color
                model.updateNote(color = color)
                model.uploadNote()
            }
        }
    }

    fun onRegisterLoginClicked(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
