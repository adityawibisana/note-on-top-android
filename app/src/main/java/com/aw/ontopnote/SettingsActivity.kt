package com.aw.ontopnote

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.aw.ontopnote.databinding.ActivitySettingBinding
import com.aw.ontopnote.helper.UserPreferences
import com.aw.ontopnote.model.event.NotePaddingSizeSettingChangedEvent
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class SettingsActivity : BaseActivity() {
    lateinit var binding: ActivitySettingBinding

    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(this)
            .setTitle(R.string.pick_padding_size)
            .setView(R.layout.dialog_seekbar)
            .setPositiveButton(R.string.close) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    private val seekBar:SeekBar? by lazy {
        dialog.findViewById<SeekBar>(R.id.seekBar)
    }

    private val tvSeekBar:TextView? by lazy {
        dialog.findViewById<TextView>(R.id.tv_seekBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvPaddingSize.text = "${UserPreferences.getNotePaddingSize()}"
    }

    fun showSeekBarDialog(v: View) {
        if (dialog.isShowing) {
            return
        }

        dialog.show()

        seekBar?.max = 24
        seekBar?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                lifecycleScope.launch (Default) {
                    launch (Main) {
                        tvSeekBar?.text = "$progress"
                    }
                    UserPreferences.setNotePaddingSize(progress)
                    EventBus.getDefault().post(NotePaddingSizeSettingChangedEvent(progress))
                }
            }

            override fun onStartTrackingTouch(sb: SeekBar?) { }
            override fun onStopTrackingTouch(sb: SeekBar?) { }
        })

        seekBar?.progress = UserPreferences.getNotePaddingSize()
    }
}
