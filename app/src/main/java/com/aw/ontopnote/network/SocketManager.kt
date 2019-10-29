package com.aw.ontopnote.network

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import android.util.Log

const val TAG = "SocketManager"
const val socketURL = "http://192.168.1.101:1337"
const val version_query = "__sails_io_sdk_version=1.2.1"

object SocketManager  {
    private val socket: Socket by lazy {
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)

        IO.socket("$socketURL?$version_query", opts)
    }

    fun connect() {
        socket.on(Socket.EVENT_CONNECT) {
            JSONObject().also {
                it.put("url", "$socketURL/ping?$version_query&data1=yoloooooooo")
                socket.emit("get", it)
            }

        }.on(Socket.EVENT_ERROR) {
            Log.v(TAG, "Socket connect error:")
        }.on(Socket.EVENT_DISCONNECT) {
            Log.v(TAG, "Socket disconnect")
        }

        socket.connect()
    }
}