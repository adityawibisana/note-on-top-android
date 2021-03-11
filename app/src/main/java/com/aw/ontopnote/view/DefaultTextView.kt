package com.aw.ontopnote.view

import CommonUtils.defaultScope
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import com.aw.commons.ScopeUtil
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.NoteDetailActivity
import com.aw.ontopnote.R
import com.aw.ontopnote.helper.Constants
import com.aw.ontopnote.helper.SingletonHolder
import com.aw.ontopnote.helper.Themes
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.model.ViewType
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class DefaultTextView private constructor(context: Context) {

    private val inflater:LayoutInflater by lazy {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
    }

    init {

    }

    companion object : SingletonHolder<DefaultTextView, Context>(::DefaultTextView)

    @SuppressLint("ClickableViewAccessibility")
    fun generateTextView(note: Note): TextView {
        val textView = inflater.inflate(R.layout.view_default_text_view, null) as TextView
        textView.tag = note

        val doubleTapListener = object : GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
            override fun onShowPress(e: MotionEvent?) { }

            override fun onDown(e: MotionEvent?): Boolean {
                //ensure we get the latest note pointer
                ScopeUtil.db.launch {
                    val latestNote = NoteRepository.getNoteById(MainApp.applicationContext(), note.id)

                    latestNote.viewType = if (latestNote.viewType == ViewType.PARTIALLY_HIDDEN) ViewType.VISIBLE else ViewType.PARTIALLY_HIDDEN
                    NoteRepository.updateNote(MainApp.applicationContext(), latestNote)
                }
                return true
            }


            override fun onSingleTapUp(e: MotionEvent?): Boolean = false

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean = false

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false

            override fun onLongPress(e: MotionEvent?) {
                val intent = Intent(textView.context, NoteDetailActivity::class.java)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.id)

                textView.context.startActivity(intent)
                Toast.makeText(textView.context, R.string.pending_wait_note_detail, Toast.LENGTH_LONG).show()
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean = false
            override fun onDoubleTapEvent(e: MotionEvent?): Boolean = false
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean = false
        }

        val gestureDetector = GestureDetector(textView.context, doubleTapListener)

        textView.setOnTouchListener { _, event ->
            // TODO Auto-generated method stub
            gestureDetector.onTouchEvent(event)
            false
        }

        return decorateTextView(textView, note)
    }

    fun decorateTextView (textView: TextView, note: Note) : TextView {
        Timber.v("note.viewType:${note.viewType}")

        when (note.viewType) {
            ViewType.GONE -> textView.visibility = View.GONE
            ViewType.PARTIALLY_HIDDEN -> {
                textView.visibility = View.VISIBLE

                textView.animate()
                    .x(-1 * textView.width + 42f)
                    .setDuration(300)
                    .withEndAction {
                        textView.text = " "
                        textView.x = 0f
                    }.start()
            }
            ViewType.VISIBLE -> {
                textView.visibility = View.VISIBLE
                textView.text = note.text
            }
        }

        var color  = Color.parseColor(Themes.defaultBackground)
        try {
            color = Color.parseColor(note.color)
        } catch (ignored: Exception) { }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            DrawableCompat.setTint(
                textView.background,
                color
            )
        } else {
            (textView.background as Drawable).mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, note.fontSize.toFloat())

        var paddingSize = Constants.DEFAULT_NOTE_PADDING_SIZE * note.fontSize / Constants.MAXIMUM_NOTE_FONT_SIZE
        if (paddingSize < Constants.MINIMUM_NOTE_PADDING_SIZE) {
            paddingSize = Constants.MINIMUM_NOTE_PADDING_SIZE
        }
        Timber.v("paddingSize:$paddingSize")

        textView.setPadding(0, paddingSize, paddingSize, paddingSize)

        return textView
    }
}