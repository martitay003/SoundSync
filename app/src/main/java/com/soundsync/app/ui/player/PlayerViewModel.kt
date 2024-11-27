package com.soundsync.app.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.soundsync.app.service.MediaPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val mediaPlayerManager: MediaPlayerManager
) : ViewModel() {

    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Initial)
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    val isPlaying: StateFlow<Boolean> = mediaPlayerManager.isPlaying
    val currentPosition: StateFlow<Long> = mediaPlayerManager.currentPosition
    val duration: StateFlow<Long> = mediaPlayerManager.duration
    val playbackState: StateFlow<Int> = mediaPlayerManager.playbackState

    private val _shuffleEnabled = MutableStateFlow(false)
    val shuffleEnabled: StateFlow<Boolean> = _shuffleEnabled.asStateFlow()

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode: StateFlow<Int> = _repeatMode.asStateFlow()

    init {
        viewModelScope.launch {
            // Update player state based on playback state
            playbackState.collect { state ->
                _playerState.value = when (state) {
                    Player.STATE_IDLE -> PlayerState.Initial
                    Player.STATE_BUFFERING -> PlayerState.Loading
                    Player.STATE_READY -> if (isPlaying.value) PlayerState.Playing else PlayerState.Paused
                    Player.STATE_ENDED -> PlayerState.Initial
                    else -> PlayerState.Initial
                }
            }
        }
    }

    fun playMediaItem(mediaItem: MediaItem) {
        viewModelScope.launch {
            mediaPlayerManager.playMediaItem(mediaItem)
        }
    }

    fun playMediaItems(mediaItems: List<MediaItem>, startIndex: Int = 0) {
        viewModelScope.launch {
            mediaPlayerManager.playMediaItems(mediaItems, startIndex)
        }
    }

    fun addToQueue(mediaItem: MediaItem) {
        viewModelScope.launch {
            mediaPlayerManager.addMediaItem(mediaItem)
        }
    }

    fun addToQueue(mediaItems: List<MediaItem>) {
        viewModelScope.launch {
            mediaPlayerManager.addMediaItems(mediaItems)
        }
    }

    fun removeFromQueue(index: Int) {
        viewModelScope.launch {
            mediaPlayerManager.removeMediaItem(index)
        }
    }

    fun clearQueue() {
        viewModelScope.launch {
            mediaPlayerManager.clearMediaItems()
        }
    }

    fun playPause() {
        when (playerState.value) {
            is PlayerState.Playing -> pause()
            else -> play()
        }
    }

    fun play() {
        viewModelScope.launch {
            mediaPlayerManager.play()
        }
    }

    fun pause() {
        viewModelScope.launch {
            mediaPlayerManager.pause()
        }
    }

    fun stop() {
        viewModelScope.launch {
            mediaPlayerManager.stop()
        }
    }

    fun skipToNext() {
        viewModelScope.launch {
            mediaPlayerManager.seekToNext()
        }
    }

    fun skipToPrevious() {
        viewModelScope.launch {
            mediaPlayerManager.seekToPrevious()
        }
    }

    fun seekTo(position: Long) {
        viewModelScope.launch {
            mediaPlayerManager.seekTo(position)
        }
    }

    fun toggleShuffle() {
        viewModelScope.launch {
            val newShuffleMode = !_shuffleEnabled.value
            _shuffleEnabled.value = newShuffleMode
            mediaPlayerManager.setShuffleMode(newShuffleMode)
        }
    }

    fun toggleRepeatMode() {
        viewModelScope.launch {
            val currentMode = _repeatMode.value
            val newMode = when (currentMode) {
                Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
                Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
                Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_OFF
                else -> Player.REPEAT_MODE_OFF
            }
            _repeatMode.value = newMode
            mediaPlayerManager.setRepeatMode(newMode)
        }
    }

    fun getCurrentMediaItemIndex(): Int = mediaPlayerManager.getCurrentMediaItemIndex()
    fun getMediaItemCount(): Int = mediaPlayerManager.getMediaItemCount()

    override fun onCleared() {
        super.onCleared()
        mediaPlayerManager.release()
    }
}

sealed class PlayerState {
    object Initial : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
    object Loading : PlayerState()
    data class Error(val message: String) : PlayerState()
}
