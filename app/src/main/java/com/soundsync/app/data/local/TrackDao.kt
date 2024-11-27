package com.soundsync.app.data.local

import androidx.room.*
import com.soundsync.app.data.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks")
    fun getAllTracks(): Flow<List<Track>>

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrackById(id: String): Track?

    @Query("SELECT * FROM tracks WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%'")
    fun searchTracks(query: String): Flow<List<Track>>

    @Query("SELECT * FROM tracks WHERE isFavorite = 1")
    fun getFavoriteTracks(): Flow<List<Track>>

    @Query("UPDATE tracks SET isFavorite = NOT isFavorite WHERE id = :trackId")
    suspend fun toggleFavorite(trackId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: Track)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<Track>)

    @Update
    suspend fun updateTrack(track: Track)

    @Query("DELETE FROM tracks WHERE id = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("DELETE FROM tracks")
    suspend fun deleteAllTracks()

    @Query("SELECT * FROM tracks WHERE isLocal = 1")
    fun getLocalTracks(): Flow<List<Track>>

    @Query("SELECT * FROM tracks WHERE isLocal = 0")
    fun getStreamingTracks(): Flow<List<Track>>

    @Query("SELECT * FROM tracks WHERE artist = :artist")
    fun getTracksByArtist(artist: String): Flow<List<Track>>

    @Query("SELECT DISTINCT artist FROM tracks")
    fun getAllArtists(): Flow<List<String>>

    @Query("SELECT * FROM tracks WHERE playCount > 0 ORDER BY playCount DESC LIMIT :limit")
    fun getMostPlayedTracks(limit: Int = 20): Flow<List<Track>>

    @Query("UPDATE tracks SET playCount = playCount + 1 WHERE id = :trackId")
    suspend fun incrementPlayCount(trackId: String)

    @Transaction
    suspend fun addToPlaylist(trackId: String, playlistId: String) {
        // This will be implemented when we add playlist support
    }
}
