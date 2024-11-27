package com.soundsync.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaStyleNotificationHelper
import com.soundsync.app.MainActivity
import com.soundsync.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "com.soundsync.app.MUSIC_CHANNEL"
    }

    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Music Playback"
            val descriptionText = "Shows the currently playing music"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    @UnstableApi
    fun getNotification(
        player: Player,
        mediaSession: MediaSession
    ): Notification {
        val mediaMetadata = player.mediaMetadata
        
        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            // Add the metadata for the currently playing track
            setContentTitle(mediaMetadata.title)
            setContentText(mediaMetadata.artist)
            setSubText(mediaMetadata.albumTitle)
            setLargeIcon(mediaMetadata.artworkData?.let { /* TODO: Convert byte array to Bitmap */ })

            // Enable launching the player by clicking the notification
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )

            // Make the transport controls visible on the lockscreen
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            // Add an app icon and set its accent color
            setSmallIcon(R.drawable.ic_music_note)
            color = context.getColor(R.color.colorPrimary)

            // Add media control buttons that invoke MediaSession actions
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_skip_previous,
                    "Previous",
                    mediaSession.getActionRequest(Player.COMMAND_SEEK_TO_PREVIOUS)
                )
            )

            if (player.isPlaying) {
                addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_pause,
                        "Pause",
                        mediaSession.getActionRequest(Player.COMMAND_PLAY_PAUSE)
                    )
                )
            } else {
                addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_play,
                        "Play",
                        mediaSession.getActionRequest(Player.COMMAND_PLAY_PAUSE)
                    )
                )
            }

            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_skip_next,
                    "Next",
                    mediaSession.getActionRequest(Player.COMMAND_SEEK_TO_NEXT)
                )
            )

            // Take advantage of MediaStyle features
            setStyle(
                MediaStyleNotificationHelper.MediaStyle()
                    .setMediaSession(mediaSession.token)
                    .setShowActionsInCompactView(0, 1, 2)
            )
        }

        return builder.build()
    }

    fun clearNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun MediaSession.getActionRequest(command: @Player.Command Int): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            command,
            Intent(context, MediaButtonReceiver::class.java).apply {
                action = when (command) {
                    Player.COMMAND_PLAY_PAUSE -> if (player.isPlaying) {
                        "ACTION_PAUSE"
                    } else {
                        "ACTION_PLAY"
                    }
                    Player.COMMAND_SEEK_TO_NEXT -> "ACTION_NEXT"
                    Player.COMMAND_SEEK_TO_PREVIOUS -> "ACTION_PREVIOUS"
                    else -> throw IllegalArgumentException("Unsupported command: $command")
                }
            },
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}
