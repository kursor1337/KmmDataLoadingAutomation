package com.kursor.kmmdataloadingautomation

import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.test.assertEquals

suspend fun <T> flowAssertEquals(expected: List<T>, flow: Flow<T>, numberOfElements: Int) = coroutineScope {
    val result = mutableListOf<T>()
    val job = launch {
        flow.collect {
            result += it
            if (result.size >= numberOfElements) {
                assertEquals(expected, result)
                this.cancel()
            }
        }
    }
    job.join()
}