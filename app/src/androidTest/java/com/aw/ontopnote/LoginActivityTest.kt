package com.aw.ontopnote

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.aw.ontopnote.util.SharedPref

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @Rule
    @JvmField
    val rule  = ActivityTestRule(LoginActivity::class.java)

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    /** normal login **/
    @Test
    fun normalLogin() {
        SharedPref.clearAll()

        ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.et_email)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))

        onView(withId(R.id.et_email)).perform(typeText("a@bc.com"))
        onView(withId(R.id.et_password)).perform(typeText("1234"))
        onView(withId(R.id.bt_login)).perform(click())

        Thread.sleep(1000)

        assertNotEquals(SharedPref.id, "")
        assertNotNull(SharedPref.email)
        assertNotNull(SharedPref.token)
    }

    @Test
    fun wrongEmailPasswordLogin() {
        SharedPref.clearAll()

        ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.et_email)).check(matches(isDisplayed()))
        onView(withId(R.id.et_password)).check(matches(isDisplayed()))

        onView(withId(R.id.et_email)).perform(typeText("a@bc.com"))
        onView(withId(R.id.et_password)).perform(typeText("1234"))
        onView(withId(R.id.bt_login)).perform(click())

        Thread.sleep(1000)

        assertNotEquals(SharedPref.id, "")
        assertNotNull(SharedPref.email)
    }
}
