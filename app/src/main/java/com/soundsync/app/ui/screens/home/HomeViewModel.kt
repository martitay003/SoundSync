package com.soundsync.app.ui.screens.home

import com.soundsync.app.core.BaseViewModel
import com.soundsync.app.core.Logger
import com.soundsync.app.core.NetworkMonitor
import com.soundsync.app.core.UiState
import com.soundsync.app.core.asResult
import com.soundsync.app.data.model.Track
import com.soundsync.app.data.repository.TrackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val trackRepository: TrackRepository,
    private val networkMonitor: NetworkMonitor,
    private val logger: Logger
) : BaseViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val tracks: StateFlow<UiState<List<Track>>> = trackRepository.getAllTracks()
        .asResult()
        .map { result ->
            when (result) {
                is com.soundsync.app.core.Result.Success -> {
                    if (result.data.isEmpty()) UiState.empty()
                    else UiState.success(result.data)
                }
                is com.soundsync.app.core.Result.Error -> {
                    logger.e("HomeViewModel", "Error fetching tracks", result.exception)
                    UiState.error("Failed to load tracks")
                }
                is com.soundsync.app.core.Result.Loading -> UiState.loading()
            }
        }
        .catch { e ->
            logger.e("HomeViewModel", "Error in tracks flow", e)
            emit(UiState.error("An unexpected error occurred"))
        }
        .stateInViewModel(UiState.loading())

    val favoriteTracks: StateFlow<UiState<List<Track>>> = trackRepository.getFavoriteTracks()
        .asResult()
        .map { result ->
            when (result) {
                is com.soundsync.app.core.Result.Success -> {
                    if (result.data.isEmpty()) UiState.empty()
                    else UiState.success(result.data)
                }
                is com.soundsync.app.core.Result.Error -> {
                    logger.e("HomeViewModel", "Error fetching favorite tracks", result.exception)
                    UiState.error("Failed to load favorite tracks")
                }
                is com.soundsync.app.core.Result.Loading -> UiState.loading()
            }
        }
        .catch { e ->
            logger.e("HomeViewModel", "Error in favorites flow", e)
            emit(UiState.error("An unexpected error occurred"))
        }
        .stateInViewModel(UiState.loading())

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateInViewModel(false)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            searchTracks(query)
        }
    }

    private fun searchTracks(query: String) {
        launchIO {
            try {
                trackRepository.searchTracks(query)
            } catch (e: Exception) {
                logger.e("HomeViewModel", "Error searching tracks", e)
            }
        }
    }

    fun toggleFavorite(trackId: String) {
        launchIO {
            try {
                trackRepository.toggleFavorite(trackId)
            } catch (e: Exception) {
                logger.e("HomeViewModel", "Error toggling favorite", e)
            }
        }
    }
}
