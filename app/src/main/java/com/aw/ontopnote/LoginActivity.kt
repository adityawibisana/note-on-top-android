package com.aw.ontopnote

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.aw.ontopnote.network.ErrorHandler
import com.aw.ontopnote.network.Service
import com.aw.ontopnote.network.SocketManager
import com.aw.ontopnote.util.SharedPref
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPref.token == null) {
            setContentView(R.layout.activity_login)
        } else {
            goToNoteActivity()
            launch (Default) {
                SocketManager.connect()
            }
        }
    }

    fun onRegisterClicked(view: View) {
        launch (Default) {
            try {
                val userReq = Service.onTopNoteService.signUp(et_email.text.toString(), et_password.text.toString()).execute()
                if (userReq.isSuccessful) {
                    SharedPref.id = userReq.body()!!.id
                    SharedPref.email = userReq.body()!!.email
                } else {
                    val errorResponse = ErrorHandler.parse(userReq.errorBody())
                    launch (Main) {
                        Toast.makeText(this@LoginActivity, errorResponse!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (exception: Exception) {
                launch (Main) {
                    Toast.makeText(this@LoginActivity, String.format(resources.getString(R.string.signup_error), exception.message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onLoginClicked(view: View) {
        launch (Default) {
            if (SharedPref.token == null) {
                val baseOS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.BASE_OS else "-"
                try {
                    val userReq = Service.onTopNoteService.login(
                        et_email.text.toString(),
                        et_password.text.toString(),
                        "${Build.MANUFACTURER} ${Build.MODEL}",
                        "$baseOS ${Build.VERSION.SDK_INT}" ).execute()

                    if (userReq.isSuccessful) {
                        SharedPref.id = userReq.body()!!.id
                        SharedPref.email = userReq.body()!!.email
                        SharedPref.token = userReq.body()!!.token

                        goToNoteActivity()
                        SocketManager.connect()
                    } else {
                        val errorResponse = ErrorHandler.parse(userReq.errorBody())
                        showDefaultErrorToast(errorResponse!!.message)
                    }
                } catch (exception: Exception) {
                    showDefaultErrorToast(String.format(resources.getString(R.string.login_error), exception.message))
                }
            }
        }
    }

    private fun showDefaultErrorToast(message: String) {
        launch (Main) {
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToNoteActivity() {
        launch (Main) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}
