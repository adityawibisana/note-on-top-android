package com.aw.ontopnote

import android.content.Context
import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        System.out.println("begin coroutine")

        GlobalScope.launch (Dispatchers.Default) {
            System.out.println("launched coroutine 1")
            Thread.sleep(5000)
            System.out.println("Here after a delay of 5 seconds")
        }

        GlobalScope.launch(Dispatchers.Default) {
            System.out.println("launched coroutine 2")
        }

        hardCalculation()
        runBlocking {
            withContext(Dispatchers.Default) {
                hardCalculation2()
            }
        }

        System.out.println("end coroutine")
    }

    private fun hardCalculation() = runBlocking {
        withContext(Dispatchers.Default) {
            delay(2000)
            System.out.println("launched coroutine inside run blocking")
        }
    }

    private suspend fun hardCalculation2() : Int {
        System.out.println("launched coroutine after delay")
        return 2000
    }
}
