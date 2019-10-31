package com.aw.ontopnote

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.aw.ontopnote.network.Service
import com.aw.ontopnote.util.SharedPref
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onRegisterClicked(view: View) {
        launch (Default) {
            try {
                val userReq = Service.onTopNoteService.signUp(et_email.text.toString(), et_password.text.toString()).execute()
                if (userReq.isSuccessful) {
                    SharedPref.id = userReq.body()!!.id
                    SharedPref.email = userReq.body()!!.email
                    SharedPref.password = userReq.body()!!.password
                } else {
                    try {
                        val error = JSONObject(userReq.errorBody()!!.string())
                        launch (Main) {
                            Toast.makeText(this@LoginActivity, error.getString("message"), Toast.LENGTH_SHORT).show()
                        }
                    } catch (ignored: Exception) { }
                }
            } catch (exception: Exception) {
                launch (Main) {
                    Toast.makeText(this@LoginActivity, String.format(resources.getString(R.string.signup_error), exception.message), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
