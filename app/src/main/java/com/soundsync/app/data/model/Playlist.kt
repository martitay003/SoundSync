package com.soundsync.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String = "",
    val coverUrl: String = "",
    val createdBy: String,
    val isPublic: Boolean = false,
    val createdAt: Long = Instant.now().epochSecond,
    val updatedAt: Long = Instant.now().epochSecond,
    val trackCount: Int = 0
)
