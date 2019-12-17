package com.aw.ontopnote

import androidx.test.runner.AndroidJUnit4
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.network.*
import com.aw.ontopnote.util.SharedPref
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
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

            launch (Default) {
                val note = SocketDBRepository.createNote( Note(text="test"))
                val noteDB = NoteRepository.getNoteById(MainApp.applicationContext(), note.id)

                //ensure local and id is found
                assertEquals(noteDB.id, note.id)
                assertEquals(noteDB.remoteId, note.remoteId)
                println("id and remoteId on localDB is now the same with the one from socket")
            }
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
                val createdNote = NoteRepository.insertNote(MainApp.applicationContext(), Note(text="test"))
                val parsedNoteFromServer = SocketDataSource.createNote(createdNote, SocketManager.socket)
                assertNotNull(parsedNoteFromServer)
                NoteRepository.updateNote(MainApp.applicationContext(), createdNote)
                //ensure the DB is writing the remote id
                val createdNoteOnDB = NoteRepository.getNoteById(MainApp.applicationContext(), createdNote.id)
                assertEquals(createdNote.remoteId, createdNoteOnDB.remoteId)

                val updatedText = UUID.randomUUID().toString()
                createdNote.text = updatedText
                SocketDataSource.updateNote(createdNote, SocketManager.socket)
                Thread.sleep(2000)

                val retrievedNote = SocketDataSource.getNote(createdNote.remoteId, SocketManager.socket)
                assertNotNull(retrievedNote)
                assertEquals(updatedText, retrievedNote?.text)
                println("retrieved text from server and local is the same")
            }

            Thread.sleep(15000)
        }
    }

    @Test
    fun shouldProcessUpdatedNote() {
        runBlocking {
            launch (Default) {
                SharedPref.token = "OaJui1dBGU"

                val isConnected = SocketManager.connect()
                assertEquals(true, isConnected)

                //should use another socket to connect, then we can get this NOTE_UPDATED
                val socket2 = IO.socket("$socketURL?$query_version&token=${SharedPref.token}", IO.Options().also { it.transports = arrayOf(WebSocket.NAME) })
                socket2.once(Socket.EVENT_CONNECT) { _->
                    JSONObject().also {
                        it.put("url", "$socketURL/join?$query_version&${SocketManager.query_token}")
                        socket2.emit("put", it)
                    }
                }
                socket2.connect()
                Thread.sleep(2000)

                val createdNote = NoteRepository.insertNote(MainApp.applicationContext(), Note(text="test"))
                val parsedNoteFromServer = SocketDataSource.createNote(createdNote, socket2)
                assertNotNull(parsedNoteFromServer)
                NoteRepository.updateNote(MainApp.applicationContext(), createdNote)
                //ensure the DB is writing the remote id
                val createdNoteOnDB = NoteRepository.getNoteById(MainApp.applicationContext(), createdNote.id)
                assertEquals(createdNote.remoteId, createdNoteOnDB.remoteId)

                val updatedText = UUID.randomUUID().toString()
                createdNote.text = updatedText
                SocketDataSource.updateNote(createdNote, socket2)
                Thread.sleep(2000)

                // local note should already be updated
                val localNote = NoteRepository.getNoteById(MainApp.applicationContext(), createdNote.id)
                assertEquals(updatedText, localNote.text)
                println("local note has been updated by another socket")
                Thread.sleep(2000)
            }

        }
    }
}