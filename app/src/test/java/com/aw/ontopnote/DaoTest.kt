package com.aw.ontopnote

import androidx.test.platform.app.InstrumentationRegistry
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.model.NotesDatabase
import com.aw.ontopnote.usecase.GetLastEditedNoteImp
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.reflect.Field


@RunWith(RobolectricTestRunner::class)
class DaoTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @After
    fun finishComponentTesting() {
        // INSTANCE is the static variable name which holds the singleton instance
        resetSingleton(NotesDatabase::class.java, "INSTANCE")
    }

    private fun resetSingleton(clazz: Class<*>, fieldName: String) {
        val instance: Field
        try {
            instance = clazz.getDeclaredField(fieldName)
            instance.isAccessible = true
            instance.set(null, null)
        } catch (e: Exception) { }
    }

    @Test
    fun insert() {
        runBlocking {
            launch (Default) {
                val firstNote = GetLastEditedNoteImp(NotesDatabase.getInstance(context).noteDao()).invoke()
                assertNotNull(firstNote)

                val firstNoteDB = NoteRepository.getNoteById(context, firstNote.id)

                assertEquals(firstNote.id, firstNoteDB.id)
                assertEquals(firstNote.text, firstNoteDB.text)
                assertEquals(firstNote.color, firstNoteDB.color)
                assertEquals(firstNote.viewType, firstNoteDB.viewType)
                assertEquals(firstNote.fontSize, firstNoteDB.fontSize)

                println("insert() test done")
            }
        }
    }

    @Test
    fun update() {
        runBlocking {
            launch (Default) {
                val updatedText = "Note is Updated"
                val note = GetLastEditedNoteImp(NotesDatabase.getInstance(context).noteDao()).invoke()
                note.text = updatedText
                NoteRepository.updateNote(context, note)

                val noteDB = NoteRepository.getNoteById(context, note.id)
                assertEquals(updatedText, noteDB.text)

                println("update() test done")
            }
        }
    }

    @Test
    fun delete() {
        runBlocking {
            launch (Default) {
                val createdNoteId = NoteRepository.insertNote(context, Note(text = "")).id
                val note = NoteRepository.getNoteById(context, createdNoteId)

                assertNotNull(note)
                assertEquals(createdNoteId, note.id)

                NoteRepository.deleteNote(context, note)

                val deletedNote = NoteRepository.getNoteById(context, createdNoteId)
                assertNull(deletedNote)

                println("delete() test done")
            }
        }
    }

    @Test
    fun deleteAllNotes() {
        runBlocking {
            launch (Default) {
                val note1Id = NoteRepository.insertNote(context, Note(text = "")).id
                assertEquals(note1Id, NoteRepository.getNoteById(context, note1Id).id)

                val note2Id = NoteRepository.insertNote(context, Note(text = "")).id
                assertEquals(note2Id, NoteRepository.getNoteById(context, note2Id).id)

                NoteRepository.deleteAllNotes(context)
                assertEquals(0, NoteRepository.getAllNotes(context).size)

                println("deleteAllNotes() test done")
            }
        }
    }

    @Test
    fun getAllNotes() {
        runBlocking {
            launch (Default) {
                NoteRepository.deleteAllNotes(context)
                NoteRepository.insertNote(context, Note(text = ""))
                NoteRepository.insertNote(context, Note(text = ""))
                NoteRepository.insertNote(context, Note(text = ""))

                val allNotes = NoteRepository.getAllNotes(context)
                assertEquals(3, allNotes.size)

                println("getAllNotes() test done")
            }
        }
    }
}