package com.soundsync.app.data.local

import androidx.room.*
import com.soundsync.app.data.model.Playlist
import com.soundsync.app.data.model.PlaylistTrackCrossRef
import com.soundsync.app.data.model.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<Playlist>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: String): Playlist?

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistWithTracks(playlistId: String): Flow<PlaylistWithTracks?>

    @Transaction
    @Query("SELECT * FROM playlists")
    fun getAllPlaylistsWithTracks(): Flow<List<PlaylistWithTracks>>

    @Query("SELECT * FROM playlists WHERE createdBy = :userId")
    fun getUserPlaylists(userId: String): Flow<List<Playlist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist)

    @Update
    suspend fun updatePlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistTrackCrossRef(crossRef: PlaylistTrackCrossRef)

    @Query("DELETE FROM playlist_track_cross_ref WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: String, trackId: String)

    @Query("SELECT COUNT(*) FROM playlist_track_cross_ref WHERE playlistId = :playlistId")
    suspend fun getPlaylistTrackCount(playlistId: String): Int

    @Query("""
        UPDATE playlists 
        SET trackCount = (
            SELECT COUNT(*) 
            FROM playlist_track_cross_ref 
            WHERE playlistId = :playlistId
        )
        WHERE id = :playlistId
    """)
    suspend fun updatePlaylistTrackCount(playlistId: String)

    @Transaction
    suspend fun addTrackToPlaylist(playlistId: String, trackId: String) {
        val position = getPlaylistTrackCount(playlistId)
        val crossRef = PlaylistTrackCrossRef(
            playlistId = playlistId,
            trackId = trackId,
            addedAt = System.currentTimeMillis(),
            position = position
        )
        insertPlaylistTrackCrossRef(crossRef)
        updatePlaylistTrackCount(playlistId)
    }

    @Query("SELECT * FROM playlists WHERE name LIKE '%' || :query || '%'")
    fun searchPlaylists(query: String): Flow<List<Playlist>>

    @Query("SELECT * FROM playlists WHERE isPublic = 1")
    fun getPublicPlaylists(): Flow<List<Playlist>>

    @Transaction
    @Query("""
        SELECT p.* FROM playlists p
        INNER JOIN playlist_track_cross_ref ref ON p.id = ref.playlistId
        WHERE ref.trackId = :trackId
    """)
    fun getPlaylistsContainingTrack(trackId: String): Flow<List<Playlist>>
}
