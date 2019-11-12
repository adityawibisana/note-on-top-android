package com.aw.ontopnote

import android.content.Intent
import android.widget.EditText
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class NoteDetailActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun note_must_be_init_after_app_launch() {
        runBlocking {
            val noteText = "TestingNoteText"
            val note = NoteRepository.insertNote(context, Note(text = noteText))
            val intent = Intent()
            intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note)

            val rule = ActivityTestRule(NoteDetailActivity::class.java)

            val noteDetailActivity = rule.launchActivity(intent)
            assertNotNull(noteDetailActivity)

            val noteEditText = noteDetailActivity?.findViewById<EditText>(R.id.et_note)
            assertEquals(noteText, noteEditText?.text.toString())
        }
    }
}