package com.soundsync.app.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    @Inject
    @Dispatcher(SoundSyncDispatchers.IO)
    lateinit var ioDispatcher: CoroutineDispatcher

    @Inject
    @Dispatcher(SoundSyncDispatchers.Main)
    lateinit var mainDispatcher: CoroutineDispatcher

    @Inject
    @Dispatcher(SoundSyncDispatchers.Default)
    lateinit var defaultDispatcher: CoroutineDispatcher

    protected fun <T> Flow<T>.stateInViewModel(
        initialValue: T,
        started: SharingStarted = SharingStarted.WhileSubscribed(5000)
    ): StateFlow<T> = stateIn(viewModelScope, started, initialValue)

    protected fun launchIO(block: suspend () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            block()
        }
    }

    protected fun launchMain(block: suspend () -> Unit) {
        viewModelScope.launch(mainDispatcher) {
            block()
        }
    }

    protected fun launchDefault(block: suspend () -> Unit) {
        viewModelScope.launch(defaultDispatcher) {
            block()
        }
    }
}
