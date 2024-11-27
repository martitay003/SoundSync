package com.soundsync.app.core

sealed interface UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
    object Loading : UiState<Nothing>
    object Empty : UiState<Nothing>

    fun isSuccess() = this is Success
    fun isError() = this is Error
    fun isLoading() = this is Loading
    fun isEmpty() = this is Empty

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(message: String) = Error(message)
        fun loading() = Loading
        fun empty() = Empty
    }
}
