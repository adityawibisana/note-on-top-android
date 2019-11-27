package com.aw.ontopnote.network

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import android.util.Log
import com.aw.ontopnote.util.SharedPref
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val TAG = "SocketManager"
const val socketURL = "http://192.168.1.101:1337"
const val query_version = "__sails_io_sdk_version=1.2.1"

object SocketManager  {
    const val TIMEOUT = 7000L
    var isConnected = false

    val socket: Socket by lazy {
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)
        opts.timeout = TIMEOUT

        IO.socket("$socketURL?$query_version&token=${SharedPref.token}", opts).apply {
            this.on(Socket.EVENT_CONNECT) {
                handleConnectEvent()
                JSONObject().also {
                    it.put("url", "$socketURL/join?$query_version&$query_token")
                    this.emit("put", it)
                }
            }.on(Socket.EVENT_ERROR) {
                Log.v(TAG, "Socket connect error:")
                handleDisconnectEvent()
            }.on(Socket.EVENT_DISCONNECT) {
                Log.v(TAG, "Socket disconnect")
                handleDisconnectEvent()
            }.on(Socket.EVENT_MESSAGE) {
                try {
                    val json = JSONObject(it[0].toString())
                    val code = json.getInt("code")
                    when (code) {
                        1001 -> {
                            // socket/API call to create note
                        }
                    }
                    Log.v(TAG, "json:$json")
                } catch (ignored: Exception) { }
            }
        }
    }

    private val query_token: String by lazy {
        "token=" + SharedPref.token
    }

    suspend fun connect() : Boolean = suspendCancellableCoroutine { continuation ->
        if (SharedPref.token != null && !isConnected) {
            socket.once(Socket.EVENT_CONNECT) {
                continuation.resume(true)
            }
            socket.connect()
            CommonUtils.runOnDefaultThread({
                if (continuation.isActive) continuation.resume(isConnected)
            }, TIMEOUT)
        } else {
            continuation.resume(false)
        }
    }

    fun handleDisconnectEvent() {
        isConnected = false
        socket.connect()
    }

    fun handleConnectEvent() {
        isConnected = true
    }
}