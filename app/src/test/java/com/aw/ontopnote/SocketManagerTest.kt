package com.aw.ontopnote

import androidx.test.runner.AndroidJUnit4
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.model.NoteRepository
import com.aw.ontopnote.network.SocketDBRepository
import com.aw.ontopnote.network.SocketManager
import com.aw.ontopnote.util.SharedPref
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SocketManagerTest {
    @Test
    fun shouldBeAbleToCreateNote() {
        runBlocking {
            //prepare the access token first
            SharedPref.token = "OaJui1dBGU"

            SocketManager.connect()
            Thread.sleep(2000)

            SocketDBRepository.createNote( Note(text="test")) {
                val noteDB = NoteRepository.getNoteById(MainApp.applicationContext(), it.id)

                //ensure local and id is found
                assertEquals(noteDB.id, it.id)
                assertEquals(noteDB.remoteId, it.remoteId)
                println("id and remoteId on localDB is now the same with the one from socket")
                Thread.currentThread().interrupt()

            }
            Thread.sleep(2000)
        }
    }
}