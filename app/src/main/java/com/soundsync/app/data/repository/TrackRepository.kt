package com.soundsync.app.data.repository

import com.soundsync.app.data.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun getAllTracks(): Flow<List<Track>>
    suspend fun getTrackById(id: String): Track?
    suspend fun searchTracks(query: String): Flow<List<Track>>
    suspend fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun toggleFavorite(trackId: String)
    suspend fun addTrack(track: Track)
    suspend fun updateTrack(track: Track)
    suspend fun deleteTrack(trackId: String)
    suspend fun getLocalTracks(): Flow<List<Track>>
    suspend fun getStreamingTracks(): Flow<List<Track>>
}
