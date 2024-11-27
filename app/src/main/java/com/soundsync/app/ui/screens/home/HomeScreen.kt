package com.soundsync.app.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.soundsync.app.data.model.Track
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val tracks by viewModel.tracks.collectAsStateWithLifecycle()
    val favoriteTracks by viewModel.favoriteTracks.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            onSearch = {},
            active = false,
            onActiveChange = {},
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search tracks...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
        ) {}

        Spacer(modifier = Modifier.height(16.dp))

        // Favorites Section
        if (favoriteTracks.isNotEmpty()) {
            Text(
                text = "Your Favorites",
                style = MaterialTheme.typography.headlineSmall
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(favoriteTracks) { track ->
                    TrackCard(
                        track = track,
                        onFavoriteClick = viewModel::toggleFavorite,
                        modifier = Modifier.width(160.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // All Tracks Section
        Text(
            text = "All Tracks",
            style = MaterialTheme.typography.headlineSmall
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(tracks) { track ->
                TrackListItem(
                    track = track,
                    onFavoriteClick = viewModel::toggleFavorite
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackCard(
    track: Track,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = { /* Navigate to track details */ }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Album Art placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = track.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            
            Text(
                text = track.artist,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )

            IconButton(
                onClick = { onFavoriteClick(track.id) }
            ) {
                Icon(
                    imageVector = if (track.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (track.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (track.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun TrackListItem(
    track: Track,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = { Text(track.title) },
        supportingContent = { Text(track.artist) },
        leadingContent = {
            // Album Art placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        },
        trailingContent = {
            IconButton(onClick = { onFavoriteClick(track.id) }) {
                Icon(
                    imageVector = if (track.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (track.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (track.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        },
        modifier = modifier
    )
}
