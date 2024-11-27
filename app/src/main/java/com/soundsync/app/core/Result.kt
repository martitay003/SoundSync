package com.soundsync.app.core

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()

    fun isSuccess() = this is Success
    fun isError() = this is Error
    fun isLoading() = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun exceptionOrNull(): Throwable? = when (this) {
        is Error -> exception
        else -> null
    }

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(exception: Throwable) = Error(exception)
        fun loading() = Loading
    }
}
