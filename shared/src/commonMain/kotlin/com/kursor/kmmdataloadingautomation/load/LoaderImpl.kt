package com.kursor.kmmdataloadingautomation.load

import com.kursor.kmmdataloadingautomation.observe.Observable
import com.kursor.kmmdataloadingautomation.observe.ObservableImpl
import com.kursor.kmmdataloadingautomation.controllers.DataController
import com.kursor.kmmdataloadingautomation.controllers.DataEventController
import com.kursor.kmmdataloadingautomation.controllers.ObserversController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

internal class LoaderImpl<T>(
    coroutineScope: CoroutineScope,
    override val refreshTime: Duration,
    override val clearTime: Duration,
    fetchData: suspend () -> T
) : Loader<T> {

    override val data: Loadable<T> get() = dataFlow.value

    private val observersController = ObserversController(coroutineScope)
    private val dataEventController = DataEventController(
        coroutineScope,
        refreshTime,
        clearTime,
        observersController.observersCountFlow
    )
    private val dataController = DataController(
        coroutineScope,
        dataEventController.dataEventFlow,
        fetchData
    )

    private val dataFlow: StateFlow<LoadableImpl<T>> get() = dataController.dataFlow

    override suspend fun loadData(): Loadable<T> {
        return dataController.loadData()
    }

    override fun observe(
        observerCoroutineScope: CoroutineScope,
        isObserverActive: StateFlow<Boolean>,
        ignoreExceptions: Boolean
    ): Observable<T> = ObservableImpl(
        dataFlow,
        observersController,
        observerCoroutineScope,
        isObserverActive,
        ignoreExceptions
    )
}