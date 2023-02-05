package com.example.kmmdataloadingautomation.load

import com.example.kmmdataloadingautomation.observe.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

interface Loader<T> {

    val refreshTime: Duration
    val clearTime: Duration

    val data: Loadable<T>?

    suspend fun loadData(): Loadable<T>

    fun observe(
        observerCoroutineScope: CoroutineScope,
        isObserverActive: StateFlow<Boolean>,
        ignoreExceptions: Boolean
    ): Observable<T>
}