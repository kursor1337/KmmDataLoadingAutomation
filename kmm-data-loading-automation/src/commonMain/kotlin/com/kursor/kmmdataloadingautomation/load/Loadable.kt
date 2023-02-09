package com.kursor.kmmdataloadingautomation.load

interface Loadable<T> {

    val state: State

    val data: T?

    val error: LoadingError?

    enum class State {
        LOADING, SUCCESS, ERROR
    }

}

