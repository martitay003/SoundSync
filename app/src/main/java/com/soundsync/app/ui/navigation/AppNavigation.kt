package com.soundsync.app.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.soundsync.app.ui.player.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val playerViewModel: PlayerViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            SoundSyncBottomNavigation(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PlayerNavigation(
                navController = navController,
                viewModel = playerViewModel,
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Screen.Home.route) {
                        // TODO: Add HomeScreen
                    }
                    composable(Screen.Library.route) {
                        // TODO: Add LibraryScreen
                    }
                    composable(Screen.Studio.route) {
                        // TODO: Add StudioScreen
                    }
                    composable(Screen.Profile.route) {
                        // TODO: Add ProfileScreen
                    }
                    composable(Screen.Search.route) {
                        // TODO: Add SearchScreen
                    }
                    composable(Screen.Settings.route) {
                        // TODO: Add SettingsScreen
                    }
                    composable(
                        route = Screen.PlaylistDetail.route,
                        arguments = listOf(navArgument("playlistId") { type = NavType.StringType })
                    ) {
                        // TODO: Add PlaylistDetailScreen
                    }
                    composable(
                        route = Screen.ArtistDetail.route,
                        arguments = listOf(navArgument("artistId") { type = NavType.StringType })
                    ) {
                        // TODO: Add ArtistDetailScreen
                    }
                    composable(
                        route = Screen.AlbumDetail.route,
                        arguments = listOf(navArgument("albumId") { type = NavType.StringType })
                    ) {
                        // TODO: Add AlbumDetailScreen
                    }
                }
            }
        }
    }
}
