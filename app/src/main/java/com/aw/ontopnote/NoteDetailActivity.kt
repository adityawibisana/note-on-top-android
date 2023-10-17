package com.aw.ontopnote

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.aw.ontopnote.model.ViewType
import com.aw.commons.AndroidUIHelper
import com.aw.ontopnote.databinding.ActivityNoteDetailBinding
import com.aw.ontopnote.databinding.DialogColorBinding
import com.aw.ontopnote.viewmodel.NoteDetailViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import timber.log.Timber

class NoteDetailActivity : BaseActivity() {
    lateinit var binding: ActivityNoteDetailBinding
    lateinit var dialogColorBinding: DialogColorBinding

    companion object {
        const val EXTRA_NOTE_ID = "extraNoteId"
    }

    private val model: NoteDetailViewModel by lazy {
        ViewModelProviders.of(this@NoteDetailActivity)[NoteDetailViewModel::class.java]
    }

    var pauseWatcher = false

    private val textWatcher: TextWatcher by lazy {
        object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
//                Log.v(TAG, "Note changed to:${s.toString()}")
                if (!pauseWatcher) {
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
                if (!pauseWatcher) {
                    model.updateNote(fontSize = progress)
                    model.uploadNote()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        }
    }

    private val onStickyNoteChangedListener: CompoundButton.OnCheckedChangeListener by lazy {
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            if (!pauseWatcher) {
                val viewType = if (isChecked) ViewType.VISIBLE else ViewType.GONE
                model.updateNote(viewType = viewType)
                model.uploadNote()
            }
        }
    }

    private val dialog: AlertDialog by lazy {
        dialogColorBinding = DialogColorBinding.inflate(layoutInflater)

        AlertDialog.Builder(this)
            .setTitle(R.string.pick_color)
            .setView(dialogColorBinding.root)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noteId = intent.getStringExtra(EXTRA_NOTE_ID)
        model.initialize(noteId!!)
        model.liveNote.observe(this, Observer {
            Timber.v( "Live note is updated")
            pauseWatcher = true

            binding.etNote.setText(it.text)
            binding.sbFontSize.progress = it.fontSize
            binding.tbAlwaysShow.isChecked = it.viewType != ViewType.GONE
            updateButtonBackground(Color.parseColor(it.color))

            pauseWatcher = false
        })

        binding.etNote.addTextChangedListener(textWatcher)
        binding.sbFontSize.setOnSeekBarChangeListener(seekBarFontSizeChangeListener)
        binding.tbAlwaysShow.setOnCheckedChangeListener(onStickyNoteChangedListener)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch (Default) {
            val note = model.getNoteValue()
            launch (Main) {
                binding.etNote.setText(note.text)
                binding.sbFontSize.progress = note.fontSize
                binding.tbAlwaysShow.isChecked = note.viewType != ViewType.GONE
                updateButtonBackground(Color.parseColor(note.color))
                pauseWatcher = false
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pauseWatcher = true
    }

    fun showColorDialog(v: View) {
        if (!dialog.isShowing) {
            dialog.show()
        }

        for (b in dialogColorBinding.root.children) {
            if (b is Button) b.setOnClickListener {
                model.updateNote(color = AndroidUIHelper.getInstance(MainApp.applicationContext()).intToColorHex((it.background as ColorDrawable).color))
                model.uploadNote()
                updateButtonBackground((it.background as ColorDrawable).color)
            }
        }
    }

    fun onRegisterLoginClicked(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun updateButtonBackground(color: Int) {
        val background = binding.btSwitchColor.background
        if (background is ShapeDrawable) {
            background.paint.color = color
        } else if (background is GradientDrawable) {
            background.setColor(color)
        } else if (background is ColorDrawable) {
            background.color = color
        }
    }
}
