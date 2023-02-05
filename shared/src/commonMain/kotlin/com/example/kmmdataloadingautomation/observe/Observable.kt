package com.example.kmmdataloadingautomation.observe

import com.example.kmmdataloadingautomation.load.Loadable
import kotlinx.coroutines.flow.Flow

interface Observable<T> {
    val flow: Flow<Loadable<T>>
}