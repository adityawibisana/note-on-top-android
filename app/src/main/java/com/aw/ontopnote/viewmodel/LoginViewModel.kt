package com.aw.ontopnote.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import com.aw.ontopnote.MainApp
import com.aw.ontopnote.R
import com.aw.ontopnote.model.Note
import com.aw.ontopnote.network.ErrorHandler
import com.aw.ontopnote.network.Service
import com.aw.ontopnote.network.SocketDBRepository
import com.aw.ontopnote.network.SocketManager
import com.aw.ontopnote.util.SharedPref
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginViewModel : ViewModel() {
    suspend fun signUp(email: String, password: String) : String? = withContext(Default) {
        try {
            val userReq = Service.onTopNoteService.signUp(email, password).execute()
            if (userReq.isSuccessful) {
                SharedPref.id = userReq.body()!!.id
                SharedPref.email = userReq.body()!!.email
                return@withContext null
            } else {
                val errorResponse = ErrorHandler.parse(userReq.errorBody())
                return@withContext errorResponse?.message
            }
        } catch (exception: Exception) {
            return@withContext String.format(MainApp.applicationContext().resources.getString(R.string.signup_error))
        }
    }

    suspend fun login(email: String, password: String) : String? = withContext(Default) {
        if (SharedPref.token == null) {
            val baseOS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.BASE_OS else "-"
            try {
                val userReq = Service.onTopNoteService.login(
                    email,
                    password,
                    "${Build.MANUFACTURER} ${Build.MODEL}",
                    "$baseOS ${Build.VERSION.SDK_INT}" ).execute()

                if (userReq.isSuccessful) {
                    SharedPref.id = userReq.body()!!.id
                    SharedPref.email = userReq.body()!!.email
                    SharedPref.token = userReq.body()!!.token
                    return@withContext null
                } else {
                    val errorResponse = ErrorHandler.parse(userReq.errorBody())
                    return@withContext errorResponse!!.message
                }
            } catch (exception: Exception) {
                return@withContext String.format(MainApp.applicationContext().resources.getString(R.string.login_error), exception.message)
            }
        }
        return@withContext null
    }

    suspend fun getLastEditedNote() : Note? = withContext(Default) {
        SocketManager.connect()
        return@withContext SocketDBRepository.getLastEditedNote()
    }
}