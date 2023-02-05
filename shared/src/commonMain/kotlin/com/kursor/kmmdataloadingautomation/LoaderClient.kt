package com.kursor.kmmdataloadingautomation

import com.kursor.kmmdataloadingautomation.load.Loader
import com.kursor.kmmdataloadingautomation.load.LoaderImpl
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration

class LoaderClient(private val coroutineScope: CoroutineScope) {

    fun <T> createLoader(
        refreshTime: Duration,
        clearTime: Duration,
        fetchData: suspend () -> T
    ): Loader<T> = LoaderImpl(coroutineScope, refreshTime, clearTime, fetchData)

}