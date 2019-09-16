package com.aw.ontopnote

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.aw.ontopnote.model.NoteRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class DBInstrumentedTest {
    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun useAppContext() {
        Assert.assertEquals("com.aw.ontopnote", appContext.packageName)
    }

    @Test
    fun getFirstNote() {
        val firstNote = NoteRepository.getOrCreateFirstNote(appContext)
        val getNoteId  = NoteRepository.getNoteById(appContext, firstNote.id)
        Assert.assertEquals(firstNote.id, getNoteId.id)
    }
}
