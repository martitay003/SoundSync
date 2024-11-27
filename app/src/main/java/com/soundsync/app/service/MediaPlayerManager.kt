package com.soundsync.app.service

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    private var currentMediaItem: MediaItem? = null
    private var positionUpdateJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _playbackState = MutableStateFlow(Player.STATE_IDLE)
    val playbackState: StateFlow<Int> = _playbackState.asStateFlow()

    val player: ExoPlayer
        get() = exoPlayer ?: ExoPlayer.Builder(context)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true
            )
            .build()
            .also {
                exoPlayer = it
                setupPlayer(it)
            }

    private fun setupPlayer(player: ExoPlayer) {
        player.apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ALL
            setHandleAudioBecomingNoisy(true)

            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _isPlaying.value = isPlaying
                    if (isPlaying) {
                        startPositionUpdates()
                    } else {
                        stopPositionUpdates()
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    _playbackState.value = state
                    when (state) {
                        Player.STATE_READY -> {
                            _duration.value = duration
                            if (player.isPlaying) {
                                startPositionUpdates()
                            }
                        }
                        Player.STATE_ENDED -> {
                            stopPositionUpdates()
                        }
                        Player.STATE_IDLE -> {
                            stopPositionUpdates()
                        }
                    }
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    _currentPosition.value = currentPosition
                }
            })
        }
    }

    private fun startPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = coroutineScope.launch {
            while (isActive) {
                _currentPosition.value = player.currentPosition
                delay(16) // Update approximately 60 times per second
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }

    fun playMediaItem(mediaItem: MediaItem) {
        currentMediaItem = mediaItem
        player.apply {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    fun playMediaItems(mediaItems: List<MediaItem>, startIndex: Int = 0) {
        player.apply {
            setMediaItems(mediaItems)
            seekToDefaultPosition(startIndex)
            prepare()
            play()
        }
    }

    fun addMediaItem(mediaItem: MediaItem) {
        player.addMediaItem(mediaItem)
    }

    fun addMediaItems(mediaItems: List<MediaItem>) {
        player.addMediaItems(mediaItems)
    }

    fun removeMediaItem(index: Int) {
        player.removeMediaItem(index)
    }

    fun clearMediaItems() {
        player.clearMediaItems()
    }

    fun pause() {
        player.pause()
    }

    fun play() {
        player.play()
    }

    fun stop() {
        player.stop()
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun seekToNext() {
        player.seekToNext()
    }

    fun seekToPrevious() {
        player.seekToPrevious()
    }

    fun setShuffleMode(enabled: Boolean) {
        player.shuffleModeEnabled = enabled
    }

    fun setRepeatMode(repeatMode: Int) {
        player.repeatMode = repeatMode
    }

    fun getCurrentMediaItemIndex(): Int = player.currentMediaItemIndex

    fun getMediaItemCount(): Int = player.mediaItemCount

    fun release() {
        stopPositionUpdates()
        coroutineScope.cancel()
        exoPlayer?.release()
        exoPlayer = null
        _isPlaying.value = false
        _currentPosition.value = 0
        _duration.value = 0
        _playbackState.value = Player.STATE_IDLE
    }
}
