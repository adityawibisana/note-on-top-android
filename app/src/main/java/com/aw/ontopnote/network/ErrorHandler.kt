package com.aw.ontopnote.network

import com.aw.ontopnote.model.ErrorResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import java.lang.Exception

object ErrorHandler {
    fun parse(responseBody: ResponseBody?) : ErrorResponse? {
        try {
            val json = JSONObject(responseBody!!.string())
            return ErrorResponse(json.getInt("code"), json.getString("message"))
        } catch (ignored: Exception) { }
        return null
    }
}