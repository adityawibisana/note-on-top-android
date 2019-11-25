package com.aw.ontopnote.network
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.util.SharedPref
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONObject
import java.lang.Exception

object SocketRepository {
    val query_token: String by lazy {
        "token=" + SharedPref.token
    }

    fun updateNote(note: Note, socket: Socket, updatedNote: (note: Note) -> Unit) {
        JSONObject().also {
            val content = Gson().toJson(note)
            it.put("url", "$socketURL/updateNote/${note.id}?$query_version&$query_token&text=$content")
            socket.emit("put", it)
        }
    }

    /**
     * @param note MUST be created first on db
     * @param socket the socket, retrieved from SocketManager
     * @param callback the callback after the note has been created
     */
    fun createNote (note: Note, socket: Socket, callback: (createdNote: Note) -> Unit) {
        JSONObject().also { it ->
            it.put("url", "$socketURL/createNote?$query_version&$query_token&localId=${note.id}")
            socket.emit("post", it).once("NOTE_CREATED") {
                try {
                    val json = JSONObject(it[0].toString())
                    val localId = json.getString("localId")
                    if (localId == note.id) {
                        note.remoteId = json.getString("id")
                        callback(note)
                    }
                } catch (ignored: Exception) { }
            }
        }
    }
}