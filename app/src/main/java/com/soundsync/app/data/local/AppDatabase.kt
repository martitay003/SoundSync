package com.soundsync.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.soundsync.app.data.model.Playlist
import com.soundsync.app.data.model.PlaylistTrackCrossRef
import com.soundsync.app.data.model.Track

@Database(
    entities = [
        Track::class,
        Playlist::class,
        PlaylistTrackCrossRef::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao

    companion object {
        const val DATABASE_NAME = "soundsync_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE tracks ADD COLUMN playCount INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
