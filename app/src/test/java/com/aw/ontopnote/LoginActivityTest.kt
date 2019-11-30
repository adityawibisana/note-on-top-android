package com.aw.ontopnote

import android.widget.EditText
import androidx.test.runner.AndroidJUnit4
import org.junit.Test
import org.robolectric.Robolectric
import org.junit.runner.RunWith
import com.aw.ontopnote.util.SharedPref
import com.aw.ontopnote.viewmodel.LoginViewModel
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class LoginTest {
    @Before
    fun clearAll() {
        SharedPref.clearAll()
    }

    @Test
    fun login() = runBlocking {
        val vm = LoginViewModel()

        val email = "a@bc.com"
        val pass = "1234"
        vm.login(email, pass)

        assertEquals(email, SharedPref.email)
        assertNotNull(SharedPref.token)

        println("login() process done")
    }

    @Test
    fun login_with_wrong_username_should_not_crash() = runBlocking{
        val activity = Robolectric.setupActivity(LoginActivity::class.java)

        val emailEditText = activity.findViewById<EditText>(R.id.et_email)
        val passwordEditText = activity.findViewById<EditText>(R.id.et_password)

        emailEditText.setText("not an email username")
        passwordEditText.setText("1234")

        val vm = LoginViewModel()
        vm.login(emailEditText.text.toString(), passwordEditText.text.toString())

        assertNull(SharedPref.token)
        println("login_with_wrong_username_should_not_crash() process done")
    }

    @Test
    fun login_with_no_password_should_not_crash() = runBlocking {
        val activity = Robolectric.setupActivity(LoginActivity::class.java)

        val emailEditText = activity.findViewById<EditText>(R.id.et_email)
        emailEditText.setText("a@bc.com")

        val vm = LoginViewModel()
        vm.login(emailEditText.text.toString(), "")

        assertNull(SharedPref.token)
        println("login_with_no_password_should_not_crash() process done")
    }
}