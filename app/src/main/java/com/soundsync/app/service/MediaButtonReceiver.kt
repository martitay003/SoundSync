package com.soundsync.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MediaButtonReceiver : BroadcastReceiver() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var controllerFuture: ListenableFuture<MediaController>? = null

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "ACTION_PLAY" -> getController(context) { it.play() }
            "ACTION_PAUSE" -> getController(context) { it.pause() }
            "ACTION_NEXT" -> getController(context) { it.seekToNext() }
            "ACTION_PREVIOUS" -> getController(context) { it.seekToPrevious() }
        }
    }

    private fun getController(context: Context, action: (MediaController) -> Unit) {
        val sessionToken = SessionToken(context, MediaPlaybackService::class.java)
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        scope.launch {
            try {
                val controller = controllerFuture?.await()
                controller?.let(action)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    override fun onDetachedFromContext() {
        controllerFuture?.let(MediaController::releaseFuture)
        super.onDetachedFromContext()
    }
}
