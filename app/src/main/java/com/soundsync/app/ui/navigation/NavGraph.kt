package com.soundsync.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soundsync.app.ui.screens.home.HomeScreen
import com.soundsync.app.ui.screens.library.LibraryScreen
import com.soundsync.app.ui.screens.profile.ProfileScreen
import com.soundsync.app.ui.screens.studio.StudioScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Library : Screen("library")
    object Studio : Screen("studio")
    object Profile : Screen("profile")
}

@Composable
fun SoundSyncNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { SoundSyncBottomNavigation(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(navController)
                }
                
                composable(Screen.Library.route) {
                    LibraryScreen(navController)
                }
                
                composable(Screen.Studio.route) {
                    StudioScreen(navController)
                }
                
                composable(Screen.Profile.route) {
                    ProfileScreen(navController)
                }
            }
        }
    }
}
