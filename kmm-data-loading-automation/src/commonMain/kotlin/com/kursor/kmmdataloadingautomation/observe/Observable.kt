package com.kursor.kmmdataloadingautomation.observe

import com.kursor.kmmdataloadingautomation.load.Loadable
import kotlinx.coroutines.flow.Flow

interface Observable<T> {
    val flow: Flow<Loadable<T>>
}