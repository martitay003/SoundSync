package com.soundsync.app.core

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: SoundSyncDispatchers)

enum class SoundSyncDispatchers {
    IO,
    Default,
    Main,
    Unconfined
}
