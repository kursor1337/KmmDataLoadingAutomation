package com.kursor.kmmdataloadingautomation.load

internal class LoadableImpl<T> private constructor(
    override val data: T?,
    override val error: LoadingError?
) : Loadable<T> {

    constructor(data: T?) : this(data, null)
    constructor(error: LoadingError?) : this(null, error)
    constructor() : this(null, null)

    override val state: Loadable.State = when {
        data != null -> Loadable.State.SUCCESS
        error != null -> Loadable.State.ERROR
        else -> Loadable.State.LOADING
    }
}