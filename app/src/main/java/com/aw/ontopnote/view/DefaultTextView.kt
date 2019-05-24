package com.aw.ontopnote.view

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.helper.SingletonHolder
import com.aw.ontopnote.helper.Utils
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import android.util.Log
import android.view.GestureDetector
import android.widget.Toast
import com.aw.ontopnote.NoteDetailActivity
import com.aw.ontopnote.R


class DefaultTextView private constructor(context: Context) {

    private val inflater:LayoutInflater by lazy {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
    }

    init {

    }

    companion object : SingletonHolder<DefaultTextView, Context>(::DefaultTextView) {
        const val TAG = "DefaultTextView"
    }

    fun generateTextView(note: Note): TextView {
        var textView = inflater.inflate(com.aw.ontopnote.R.layout.view_default_text_view, null) as TextView
        textView.tag = note

        val doubleTapListener = object : GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
            override fun onShowPress(e: MotionEvent?) { }

            override fun onSingleTapUp(e: MotionEvent?): Boolean { return false }

            override fun onDown(e: MotionEvent?): Boolean {
                //ensure we get the latest note pointer
                val latestNote = NoteRepository.getNoteById(MainApp.applicationContext(), note.id)

                latestNote.isHidden = !latestNote.isHidden
                NoteRepository.updateNote(MainApp.applicationContext(), latestNote)
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean { return false }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean { return false }

            override fun onLongPress(e: MotionEvent?) {
                val intent = Intent(textView.context, NoteDetailActivity::class.java)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.id)

                //ensure we get the latest note pointer
                val latestNote = NoteRepository.getNoteById(MainApp.applicationContext(), note.id)
                latestNote.isHidden = false
                NoteRepository.updateNote(MainApp.applicationContext(), latestNote)

                textView.context.startActivity(intent)
                Toast.makeText(textView.context, R.string.pending_wait_note_detail, Toast.LENGTH_LONG).show()
            }


            override fun onDoubleTap(e: MotionEvent?): Boolean { return false }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean { return false }
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean { return false }
        }

        val gestureDetector = GestureDetector(textView.context, doubleTapListener)

        textView.setOnTouchListener(View.OnTouchListener { v, event ->
            // TODO Auto-generated method stub
            gestureDetector.onTouchEvent(event)
            false
        })

        return decorateTextView(textView, note)
    }

    fun decorateTextView (textView: TextView, note: Note) : TextView {
        textView.visibility = if (note.content.isBlank() && !note.isHidden) View.GONE else View.VISIBLE

        textView.text = if (note.isHidden) "" else note.content

        DrawableCompat.setTint(
            textView.background,
            Utils.rgbToColorRes(MainApp.applicationContext(), note.color)
        )

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, note.fontSize.toFloat())

        return textView
    }
}