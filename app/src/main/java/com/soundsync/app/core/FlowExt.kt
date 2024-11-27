package com.soundsync.app.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> { Result.success(it) }
        .onStart { emit(Result.loading()) }
        .catch { emit(Result.error(it)) }
}

fun <T> Flow<Result<T>>.onSuccess(action: suspend (T) -> Unit): Flow<Result<T>> {
    return this.map { result ->
        if (result is Result.Success) {
            action(result.data)
        }
        result
    }
}

fun <T> Flow<Result<T>>.onError(action: suspend (Throwable) -> Unit): Flow<Result<T>> {
    return this.map { result ->
        if (result is Result.Error) {
            action(result.exception)
        }
        result
    }
}

fun <T> Flow<Result<T>>.onLoading(action: suspend () -> Unit): Flow<Result<T>> {
    return this.map { result ->
        if (result is Result.Loading) {
            action()
        }
        result
    }
}
