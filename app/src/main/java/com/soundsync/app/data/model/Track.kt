package com.soundsync.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val artist: String,
    val albumArt: String? = null,
    val duration: Long = 0L,
    val url: String? = null,
    val localPath: String? = null,
    val genre: String? = null,
    val album: String? = null,
    val year: Int? = null,
    val trackNumber: Int? = null,
    val isLocal: Boolean = false,
    val isFavorite: Boolean = false,
    val playCount: Int = 0
)
