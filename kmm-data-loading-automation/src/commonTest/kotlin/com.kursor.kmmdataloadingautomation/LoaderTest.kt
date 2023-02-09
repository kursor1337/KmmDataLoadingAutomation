package com.kursor.kmmdataloadingautomation

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class LoaderTest {

    val coroutineScope = CoroutineScope(Job() + Dispatchers.Default)
    val loaderClient = LoaderClient(coroutineScope)

    val data = mutableListOf(1, 2, 3, 4, 5)

    @Test
    fun loadTest() {
        runBlocking {
            var count = 0
            val loader = loaderClient.createLoader(100.milliseconds, 1.seconds) {
                if (count == data.size) count = 0
                data[count++]
            }
            val observerActive = MutableStateFlow(true)
            val value = loader.observe(coroutineScope, observerActive, false)
            flowAssertEquals(
                listOf(1, 2, 3, 4, 5, 1, 2, 3, 4, 5),
                value.flow.mapNotNull { it.data },
                10

            )
        }
    }

    @Test
    fun clearTest() {
        runBlocking {
            var count = 0
            val loader = loaderClient.createLoader(100.milliseconds, 1.seconds) {
                if (count == data.size) count = 0
                data[count++]
            }

            val observerActive = MutableStateFlow(true)
            val value = loader.observe(coroutineScope, observerActive, false)

            flowAssertEquals(
                listOf(1, 2, 3),
                value.flow.mapNotNull { it.data },
                3
            ) // check for loading
            observerActive.value = false
            for (i in 5 until 15) {
                assertEquals(3, loader.data.data)
                delay(100)
            } // check that data isn't cleared for "clearTime" after observers are gone
            assertNull(loader.data.data) // check that data is cleared after "clearTime" (here it is 1 second)
        }
    }

    @Test
    fun observersTest() {
        runBlocking {
            var count = 0
            val loader = loaderClient.createLoader(1.seconds, 1.seconds) {
                if (count == data.size) count = 0
                data[count++]
            }
            val observerActive1 = MutableStateFlow(true)
            val observerActive2 = MutableStateFlow(true)
            val value1 = loader.observe(coroutineScope, observerActive1, false)
            delay(100) // delay to give loader some time to load the data
            val prev = loader.data.data // read this data
            delay(500) // delay for half a refreshTime
            val value2 = loader.observe(coroutineScope, observerActive2, false)
            delay(100)  // observe again
            val cur = loader.data.data // read data
            assertEquals(1, prev) // check that the data is now updated
            assertEquals(2, cur)  // (loader should load new data when there is new observer)
            observerActive2.value = false    //
            delay(500)              // check that the loader does not load new data
            val next = loader.data.data      // when there is removing of the observer instead of adding
            assertEquals(2, next)   //
        }
    }
}