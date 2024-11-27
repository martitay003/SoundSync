package com.soundsync.app.core

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Logger @Inject constructor() {
    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun w(tag: String, message: String, throwable: Throwable? = null) {
        Log.w(tag, message, throwable)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }

    companion object {
        private const val TAG = "SoundSync"

        fun d(message: String) = Log.d(TAG, message)
        fun i(message: String) = Log.i(TAG, message)
        fun w(message: String, throwable: Throwable? = null) = Log.w(TAG, message, throwable)
        fun e(message: String, throwable: Throwable? = null) = Log.e(TAG, message, throwable)
    }
}
