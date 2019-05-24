package com.aw.ontopnote

import CommonUtils.runOnDefaultThread
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import com.aw.ontopnote.helper.Utils
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.view.DefaultTextView
import kotlinx.android.synthetic.main.activity_note_detail.*
import kotlinx.android.synthetic.main.dialog_color.*
import org.greenrobot.eventbus.EventBus
import com.aw.ontopnote.model.ViewType

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extraNoteId"
        const val TAG = "NoteDetailActivity"
    }

    private val defaultTextView: DefaultTextView by lazy {
        DefaultTextView.getInstance(this)
    }

    private val textWatcher: TextWatcher by lazy {
        object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                runOnDefaultThread({
                    if (::note.isInitialized) {
                        note.content = s.toString()

                        //if it is hidden, stay hidden
                        if (note.viewType != ViewType.GONE) {
                            note.viewType = ViewType.VISIBLE
                        }

                        NoteRepository.updateNote(applicationContext, note)
                    }
                })
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
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

    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        CommonUtils.runOnDefaultThread({
            note = NoteRepository.getNoteById(MainApp.applicationContext(), intent.getStringExtra(EXTRA_NOTE_ID))
//            val noteTextView = tv_note as TextView
//            defaultTextView.decorateTextView(noteTextView, note)

            et_note.setText(note.content)

            val progress = note.fontSize
            sb_font_size.progress = progress

            // TODO: update the value based on current note seek bar progress.
            sb_font_size.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    note.fontSize = progress
                    NoteRepository.updateNote(applicationContext, note)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) { }
                override fun onStopTrackingTouch(seekBar: SeekBar?) { }
            })

            CommonUtils.runOnUIThread({
                tb_always_show.isChecked = note.viewType != ViewType.GONE
            })

            tb_always_show.setOnCheckedChangeListener { _, isChecked ->
                note.viewType = if (isChecked) ViewType.VISIBLE else ViewType.GONE
                NoteRepository.updateNote(applicationContext, note)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        et_note.addTextChangedListener(textWatcher)
    }

    override fun onPause() {
        super.onPause()
        et_note.removeTextChangedListener(textWatcher)
        EventBus.getDefault().unregister(this)
    }

    fun showColorDialog(v: View) {
        if (!dialog.isShowing) {
            dialog.show()
        }

        if (!::note.isInitialized) {
            return
        }

        CommonUtils.runOnDefaultThread({
            for (b in dialog.dialog_color_root.children) {
                if (b is Button) b.setOnClickListener {
                    val color = (it.background as ColorDrawable).color

                    note.color = Utils.rgbToColorRes(this, color)

                    NoteRepository.updateNote(applicationContext, note)
                }
            }
        })
    }
}
