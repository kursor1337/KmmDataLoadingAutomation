package com.example.kmmdataloadingautomation.load

class LoadingError(
    val message: String?,
    val exception: Exception
) {
    constructor(exception: Exception) : this(exception.message, exception)
}