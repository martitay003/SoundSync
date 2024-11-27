package com.soundsync.app.data.repository

import com.soundsync.app.data.local.TrackDao
import com.soundsync.app.data.model.Track
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackRepositoryImpl @Inject constructor(
    private val trackDao: TrackDao
) : TrackRepository {
    override suspend fun getAllTracks(): Flow<List<Track>> = trackDao.getAllTracks()

    override suspend fun getTrackById(id: String): Track? = trackDao.getTrackById(id)

    override suspend fun searchTracks(query: String): Flow<List<Track>> = trackDao.searchTracks(query)

    override suspend fun getFavoriteTracks(): Flow<List<Track>> = trackDao.getFavoriteTracks()

    override suspend fun toggleFavorite(trackId: String) = trackDao.toggleFavorite(trackId)

    override suspend fun addTrack(track: Track) = trackDao.insertTrack(track)

    override suspend fun updateTrack(track: Track) = trackDao.updateTrack(track)

    override suspend fun deleteTrack(trackId: String) = trackDao.deleteTrack(trackId)

    override suspend fun getLocalTracks(): Flow<List<Track>> = trackDao.getLocalTracks()

    override suspend fun getStreamingTracks(): Flow<List<Track>> = trackDao.getStreamingTracks()
}
