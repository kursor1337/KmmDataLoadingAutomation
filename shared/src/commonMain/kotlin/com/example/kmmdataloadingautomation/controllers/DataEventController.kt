package com.example.kmmdataloadingautomation.controllers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration

internal class DataEventController(
    private val coroutineScope: CoroutineScope,
    private val refreshTime: Duration,
    private val clearTime: Duration,
    private val observersCountFlow: Flow<Int>
) {

    val dataEventFlow = MutableSharedFlow<DataController.Event>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private var currentObserversCount = 0
    private var previousObserversCount = 0

    init {
        coroutineScope.launch {
            observersCountFlow.collect {
                previousObserversCount = currentObserversCount
                currentObserversCount = it
                restartLoopIfNeeded()
                startClearDataTimerIfNeeded()
                stopClearDataTimerIfNeeded()
            }
        }
    }

    private var loopTimerJob: Job? = null
    private var clearDataTimerJob: Job? = null

    private fun restartLoop() {
        loopTimerJob?.cancel()
        loopTimerJob = coroutineScope.launch {
            while (true) {
                loadDataIfNeeded()
                delay(refreshTime)
            }
        }
    }

    private fun restartLoopIfNeeded() {
        if (currentObserversCount > 0 && currentObserversCount > previousObserversCount) restartLoop()
    }

    private fun startClearDataTimer() {
        clearDataTimerJob = coroutineScope.launch {
            delay(clearTime)
            dataEventFlow.emit(DataController.Event.CLEAR)
        }
    }

    private fun startClearDataTimerIfNeeded() {
        if (currentObserversCount < 1) startClearDataTimer()
    }

    private fun stopClearDataTimer() {
        clearDataTimerJob?.cancel()
    }

    private fun stopClearDataTimerIfNeeded() {
        if (currentObserversCount > 0) stopClearDataTimer()
    }

    private suspend fun loadDataIfNeeded() {
        if (currentObserversCount > 0) loadData()
    }

    private suspend fun loadData() {
        dataEventFlow.emit(DataController.Event.LOAD)
    }

}