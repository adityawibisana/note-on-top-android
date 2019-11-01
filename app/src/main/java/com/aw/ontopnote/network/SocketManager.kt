package com.aw.ontopnote.network

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import android.util.Log
import com.aw.ontopnote.util.SharedPref
import java.lang.Exception

const val TAG = "SocketManager"
const val socketURL = "http://192.168.1.101:1337"
const val query_version = "__sails_io_sdk_version=1.2.1"

object SocketManager  {
    private val socket: Socket by lazy {
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)

        IO.socket("$socketURL?$query_version", opts)
    }

    private val query_token: String by lazy {
        "token=" + SharedPref.token
    }

    fun connect() {
        socket.on(Socket.EVENT_CONNECT) {
            JSONObject().also {
                it.put("url", "$socketURL/join?$query_version&$query_token")
                socket.emit("put", it)
            }
        }.on(Socket.EVENT_ERROR) {
            Log.v(TAG, "Socket connect error:")
        }.on(Socket.EVENT_DISCONNECT) {
            Log.v(TAG, "Socket disconnect")
        }.on(Socket.EVENT_MESSAGE) {
            try {
                val json = JSONObject(it[0].toString())
                val code = json.getInt("code")
            } catch (ignored: Exception) { }
        }

        socket.connect()
    }
}