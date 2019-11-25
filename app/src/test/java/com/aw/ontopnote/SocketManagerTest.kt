package com.aw.ontopnote

import androidx.test.runner.AndroidJUnit4
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.network.SocketDBRepository
import com.aw.ontopnote.network.SocketManager
import com.aw.ontopnote.network.SocketRepository
import com.aw.ontopnote.util.SharedPref
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class SocketManagerTest {
    @Test
    fun shouldBeAbleToCreateNote() {
        runBlocking {
            //prepare the access token first
            SharedPref.token = "OaJui1dBGU"

            SocketManager.connect()
            Thread.sleep(2000)

            launch (Default) {
                val note = SocketDBRepository.createNote( Note(text="test"))
                val noteDB = NoteRepository.getNoteById(MainApp.applicationContext(), note.id)

                //ensure local and id is found
                assertEquals(noteDB.id, note.id)
                assertEquals(noteDB.remoteId, note.remoteId)
                println("id and remoteId on localDB is now the same with the one from socket")
            }
            Thread.sleep(2000)
        }
    }

    @Test
    fun shouldBeAbleToUpdateNote() {
        runBlocking {
            //prepare the access token first
            SharedPref.token = "OaJui1dBGU"

            SocketManager.connect()
            Thread.sleep(2000)

            launch (Default) {
                val createdNote = SocketDBRepository.createNote( Note(text="test"))
                val updatedText = UUID.randomUUID().toString()
                createdNote.text = updatedText
                SocketRepository.updateNote(createdNote, SocketManager.socket)
                Thread.sleep(2000)

                val retrievedNote = SocketRepository.getNote(createdNote.remoteId, SocketManager.socket)
                assertNotNull(retrievedNote)
                assertEquals(updatedText, retrievedNote?.text)
                println("retrieved text from server and local is the same")
            }

            Thread.sleep(15000)
        }
    }
}