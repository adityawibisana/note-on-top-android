package com.aw.ontopnote

import androidx.test.platform.app.InstrumentationRegistry
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DaoTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun insert() {
        runBlocking {
            val firstNote = NoteRepository.getOrCreateFirstNote(context)
            assertNotNull(firstNote)

            val firstNoteDB = NoteRepository.getNoteById(context, firstNote.id)

            assertEquals(firstNote.id, firstNoteDB.id)
            assertEquals(firstNote.text, firstNoteDB.text)
            assertEquals(firstNote.color, firstNoteDB.color)
            assertEquals(firstNote.viewType, firstNoteDB.viewType)
            assertEquals(firstNote.fontSize, firstNoteDB.fontSize)
            assertEquals(firstNote.textColor, firstNoteDB.textColor)

            println("Executed through here")
        }
    }

    @Test
    fun update() {
        runBlocking {
            val updatedText = "Note is Updated"
            val note = NoteRepository.getOrCreateFirstNote(context)
            note.text = updatedText
            NoteRepository.updateNote(context, note)

            val noteDB = NoteRepository.getNoteById(context, note.id)
            assertEquals(updatedText, noteDB.text)

            println("Executed through here")
        }
    }

    @Test
    fun delete() {
        runBlocking {
            val createdNoteId = NoteRepository.insertNote(context, Note(text = ""))
            val note = NoteRepository.getNoteById(context, createdNoteId)

            assertNotNull(note)
            assertEquals(createdNoteId, note.id)

            NoteRepository.deleteNote(context, note)

            val deletedNote = NoteRepository.getNoteById(context, createdNoteId)
            assertNull(deletedNote)
        }

    }
}