package com.soundsync.app.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    val darkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.DARK_THEME] ?: false
    }

    val audioQuality: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.AUDIO_QUALITY] ?: "HIGH"
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.DARK_THEME] = enabled
        }
    }

    suspend fun setAudioQuality(quality: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.AUDIO_QUALITY] = quality
        }
    }

    private object PreferenceKeys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val AUDIO_QUALITY = stringPreferencesKey("audio_quality")
    }
}
