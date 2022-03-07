package com.imadev.foody.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


inline fun <T> safeFirebaseCall(
    crossinline firebaseCall: suspend () -> T
) = flow {

    emit(Resource.Loading(null))

    try {
        emit(Resource.Success(firebaseCall.invoke()))
    } catch (throwable: Throwable) {
        emit(Resource.Error(throwable, null))
    }
}
