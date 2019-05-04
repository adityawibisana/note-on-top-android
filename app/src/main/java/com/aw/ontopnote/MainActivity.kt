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
import com.aw.ontopnote.model.Note
import kotlinx.android.synthetic.main.activity_main.*
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.model.event.UpdateNoteEvent
import org.greenrobot.eventbus.EventBus
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.children
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

                        EventBus.getDefault().post(UpdateNoteEvent(firstNote))
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

    fun showColorDialog(v: View) {
        if (!dialog.isShowing) {
            dialog.show()
        }

        CommonUtils.runOnDefaultThread({
            for (b in dialog.dialog_color_root.children) {
                if (b is Button) b.setOnClickListener {
                    val color = (it.background as ColorDrawable).color

                    firstNote.color = when (color) {
                        ContextCompat.getColor(this, R.color.redMaterial) -> ContextCompat.getColor(this, R.color.redMaterial)
                        ContextCompat.getColor(this, R.color.pinkMaterial) -> ContextCompat.getColor(this, R.color.pinkMaterial)
                        ContextCompat.getColor(this, R.color.purpleMaterial) -> ContextCompat.getColor(this, R.color.purpleMaterial)
                        ContextCompat.getColor(this, R.color.deepPurpleMaterial) -> ContextCompat.getColor(this, R.color.deepPurpleMaterial)
                        ContextCompat.getColor(this, R.color.indigoMaterial) -> ContextCompat.getColor(this, R.color.indigoMaterial)
                        ContextCompat.getColor(this, R.color.lightBluetMaterial) -> ContextCompat.getColor(this, R.color.lightBluetMaterial)
                        ContextCompat.getColor(this, R.color.cyanMaterial) -> ContextCompat.getColor(this, R.color.cyanMaterial)
                        ContextCompat.getColor(this, R.color.tealMaterial) -> ContextCompat.getColor(this, R.color.tealMaterial)
                        ContextCompat.getColor(this, R.color.greenMaterial) -> ContextCompat.getColor(this, R.color.greenMaterial)
                        ContextCompat.getColor(this, R.color.lightGreenMaterial) -> ContextCompat.getColor(this, R.color.lightGreenMaterial)
                        ContextCompat.getColor(this, R.color.limeMaterial) -> ContextCompat.getColor(this, R.color.limeMaterial)
                        ContextCompat.getColor(this, R.color.yellowMaterial) -> ContextCompat.getColor(this, R.color.yellowMaterial)
                        ContextCompat.getColor(this, R.color.amberMaterial) -> ContextCompat.getColor(this, R.color.amberMaterial)
                        ContextCompat.getColor(this, R.color.orangeMaterial) -> ContextCompat.getColor(this, R.color.orangeMaterial)
                        ContextCompat.getColor(this, R.color.deepOrangeMaterial) -> ContextCompat.getColor(this, R.color.deepOrangeMaterial)
                        ContextCompat.getColor(this, R.color.brownMaterial) -> ContextCompat.getColor(this, R.color.brownMaterial)
                        ContextCompat.getColor(this, R.color.greyMaterial) -> ContextCompat.getColor(this, R.color.greyMaterial)
                        ContextCompat.getColor(this, R.color.blueGreyMaterial) -> ContextCompat.getColor(this, R.color.blueGreyMaterial)
                        else -> {
                            ContextCompat.getColor(this, R.color.blueMaterial)
                        }
                    }

                    NoteRepository.updateNote(applicationContext, firstNote)
                    EventBus.getDefault().post(UpdateNoteEvent(firstNote))
                }
            }
        })
    }
}
