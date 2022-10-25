package com.imadev.foody.utils

sealed class Resource<out T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>() : Resource<T>()
    class Error<T>(throwable: Throwable?, data: T? = null) : Resource<T>(data, throwable)
}



