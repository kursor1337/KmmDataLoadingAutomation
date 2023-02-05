package com.example.kmmdataloadingautomation.controllers

import com.example.kmmdataloadingautomation.load.Loadable
import com.example.kmmdataloadingautomation.load.LoadableImpl
import com.example.kmmdataloadingautomation.load.LoadingError
import com.example.kmmdataloadingautomation.controllers.DataController.Event.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class DataController<T>(
    private val coroutineScope: CoroutineScope,
    private val eventFlow: Flow<Event>,
    private val fetchData: suspend () -> T
) {

    enum class Event {
        CLEAR, LOAD
    }

    val dataFlow = MutableStateFlow(LoadableImpl<T>())

    val data: Loadable<T> get() = dataFlow.value

    init {
        coroutineScope.launch {
            eventFlow.collect {
                when (it) {
                    CLEAR -> clearData()
                    LOAD -> loadData()
                }
            }
        }
    }

    suspend fun clearData() {
        dataFlow.emit(LoadableImpl())
    }

    suspend fun loadData(): Loadable<T> {
        val data = try {
            val temp = LoadableImpl(fetchData())
            temp
        } catch (e: Exception) {
            e.printStackTrace()
            LoadableImpl(error = LoadingError(e))
        }
        dataFlow.emit(data)
        return data
    }

}