package com.soundsync.app.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlaybackService : MediaSessionService() {

    @Inject
    lateinit var mediaPlayerManager: MediaPlayerManager

    @Inject
    lateinit var notificationManager: MediaNotificationManager

    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        
        // Initialize ExoPlayer
        val player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true
            )
            .build()

        // Set up MediaSession
        mediaSession = MediaSession.Builder(this, player)
            .setCallback(object : MediaSession.Callback {
                override fun onAddMediaItems(
                    mediaSession: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    mediaItems: List<MediaItem>
                ): List<MediaItem> {
                    // Handle media items here
                    return mediaItems
                }
            })
            .build()

        // Set up player listeners
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        // Handle buffering state
                    }
                    Player.STATE_READY -> {
                        // Handle ready state
                    }
                    Player.STATE_ENDED -> {
                        // Handle playback ended
                    }
                    Player.STATE_IDLE -> {
                        // Handle idle state
                    }
                }
                updateNotification()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateNotification()
            }
        })
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? = mediaSession

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player
        if (player == null || !player.playWhenReady || player.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    private fun updateNotification() {
        val player = mediaSession?.player ?: return
        val notification = notificationManager.getNotification(
            player = player,
            mediaSession = mediaSession!!
        )
        startForeground(MediaNotificationManager.NOTIFICATION_ID, notification)
    }
}
