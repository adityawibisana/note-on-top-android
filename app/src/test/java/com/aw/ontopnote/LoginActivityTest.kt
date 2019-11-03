package com.aw.ontopnote

import android.widget.Button
import android.widget.EditText
import androidx.test.runner.AndroidJUnit4
import org.junit.Test
import org.robolectric.Robolectric
import org.junit.runner.RunWith
import com.aw.ontopnote.util.SharedPref
import junit.framework.Assert.*
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class LoginTest {
    @Before
    fun clearAll() {
        SharedPref.clearAll()
    }

    @Test
    @Throws(Exception::class)
    fun login() {
        val activity = Robolectric.setupActivity(LoginActivity::class.java)

        val loginButton = activity.findViewById<Button>(R.id.bt_login)
        val emailEditText = activity.findViewById<EditText>(R.id.et_email)
        val passwordEditText = activity.findViewById<EditText>(R.id.et_password)

        emailEditText.setText("a@bc.com")
        passwordEditText.setText("1234")
        loginButton.performClick()

        Thread.sleep(1000)

        assertEquals(emailEditText.text.toString(), SharedPref.email)
        assertNotNull(SharedPref.token)
    }

    @Test
    fun login_with_wrong_username_should_not_crash() {
        val activity = Robolectric.setupActivity(LoginActivity::class.java)

        val loginButton = activity.findViewById<Button>(R.id.bt_login)
        val emailEditText = activity.findViewById<EditText>(R.id.et_email)
        val passwordEditText = activity.findViewById<EditText>(R.id.et_password)

        emailEditText.setText("not an email username")
        passwordEditText.setText("1234")
        loginButton.performClick()

        Thread.sleep(1000)
        assertNull(SharedPref.token)
    }

    @Test
    fun login_with_no_password_should_not_crash() {
        val activity = Robolectric.setupActivity(LoginActivity::class.java)

        val loginButton = activity.findViewById<Button>(R.id.bt_login)
        val emailEditText = activity.findViewById<EditText>(R.id.et_email)

        emailEditText.setText("a@bc.com")
        loginButton.performClick()

        Thread.sleep(1000)
        assertNull(SharedPref.token)
    }
}