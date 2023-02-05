package com.example.kmmdataloadingautomation.observe

import com.example.kmmdataloadingautomation.controllers.ObserversController
import com.example.kmmdataloadingautomation.load.Loadable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ObservableImpl<T>(
    dataFlow: StateFlow<Loadable<T>>,
    private val observersController: ObserversController,
    observerCoroutineScope: CoroutineScope,
    private val isObserverActive: StateFlow<Boolean>,
    private val ignoreExceptions: Boolean
) : Observable<T> {

    private val id = observersController.generateIdForNewObserver()

    private var isActive = isObserverActive.value

    init {
        observerCoroutineScope.launch {
            try {
                isObserverActive.collect {
                    isActive = it
                    if (it) observersController.onObserverAdded(id)
                    else observersController.onObserverRemoved(id)
                }
            } finally {
                withContext(NonCancellable) {
                    isActive = false
                    observersController.onObserverRemoved(id)
                }
            }
        }
    }

    override val flow: Flow<Loadable<T>> = dataFlow.filter {
        isActive && !(ignoreExceptions && it.state == Loadable.State.ERROR)
    }
}