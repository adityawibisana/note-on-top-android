package com.aw.ontopnote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.aw.ontopnote.util.SharedPref
import com.aw.ontopnote.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {

    private val model: LoginViewModel by lazy {
        ViewModelProviders.of(this@LoginActivity)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPref.token == null) {
            setContentView(R.layout.activity_login)
        } else {
            goToNoteActivity()
            finishAffinity()
        }
    }

    fun onRegisterClicked(view: View) {
        launch (Default) {
            val errorMessage = model.signUp(et_email.text.toString(), et_password.text.toString())
            launch (Main) {
                if (errorMessage != null) {
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun onLoginClicked(view: View) {
        launch (Default) {
            val errorMessage = model.login(et_email.text.toString(), et_password.text.toString())

            if (errorMessage == null) {
                val lastEditedNote = model.getLastEditedNote()
                if (lastEditedNote == null) {
                    goToNoteActivity()
                } else {
                    goToNoteDetailActivity(lastEditedNote.id)
                }
            } else {
                showDefaultErrorToast(errorMessage)
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

    fun goToNoteDetailActivity(noteId: String) {
        launch (Main) {
            val intent = Intent(this@LoginActivity, NoteDetailActivity::class.java)
            intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, noteId)
            startActivity(intent)
            finishAffinity()
        }
    }
}
