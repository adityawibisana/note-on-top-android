package com.aw.ontopnote

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.graphics.drawable.DrawableCompat
import com.aw.ontopnote.databinding.ViewMainBinding
import com.aw.ontopnote.model.event.UpdateNoteEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber


class MainView(context: Context) : RelativeLayout(context) {

    lateinit var binding: ViewMainBinding

    private var isExpanded = true
    private var lastClickTimeStamp = 0.toLong()

    init {
        val layoutInflater = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        binding = ViewMainBinding.inflate(layoutInflater)

        binding.textToShow.setOnClickListener {
            switchTextPosition()

            if (isDoubleClick(System.currentTimeMillis() ,lastClickTimeStamp)) {
                Timber.v("Double Clicked")

                val mainActivityIntent = Intent(context, MainActivity::class.java)
                mainActivityIntent.addCategory(Intent.CATEGORY_HOME)
                mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(mainActivityIntent)
            } else {
                Timber.v("Clicked")
            }
            lastClickTimeStamp = System.currentTimeMillis()
        }

        EventBus.getDefault().register(this)
    }

    @SuppressLint("ResourceType")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UpdateNoteEvent) {
        binding.textToShow.text = event.note.text
        DrawableCompat.setTint(
            binding.textToShow.background,
            Color.parseColor(event.note.color)
        )

        if (!isExpanded) {
            switchTextPosition()
        }
    }

    private fun isDoubleClick(new: Long, old: Long) = new - old < 300

    private fun switchTextPosition() {
        isExpanded = !isExpanded
        val targetX = if (isExpanded) 0.0f else -1 * binding.textToShow.width + 35.0f

        ObjectAnimator.ofFloat(binding.textToShow, "translationX", targetX).apply {
            duration = 300
            start()
        }
    }

}