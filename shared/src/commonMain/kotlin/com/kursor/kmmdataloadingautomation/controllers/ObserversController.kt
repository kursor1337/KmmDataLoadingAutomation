package com.kursor.kmmdataloadingautomation.controllers

import com.kursor.kmmdataloadingautomation.observe.ObserverId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class ObserversController(
    private val coroutineScope: CoroutineScope
) {

    val observersCountFlow = MutableStateFlow(0)
    private val observersList = mutableListOf<ObserverId>()

    private var count = 0

    fun onObserverAdded(id: ObserverId) {
        coroutineScope.launch {
            observersList.add(id)
            observersCountFlow.emit(observersList.size)
        }
    }

    fun onObserverRemoved(id: ObserverId) {
        coroutineScope.launch {
            observersList.remove(id)
            observersCountFlow.emit(observersList.size)
        }
    }

    fun generateIdForNewObserver(): ObserverId {
        return ObserverId(count++)
    }

}