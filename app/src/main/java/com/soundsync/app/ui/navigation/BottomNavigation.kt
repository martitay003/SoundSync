package com.soundsync.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(Screen.Home.route, Icons.Default.Home, "Home")
    object Library : BottomNavItem(Screen.Library.route, Icons.Default.LibraryMusic, "Library")
    object Studio : BottomNavItem(Screen.Studio.route, Icons.Default.QueueMusic, "Studio")
    object Profile : BottomNavItem(Screen.Profile.route, Icons.Default.Person, "Profile")
}

@Composable
fun SoundSyncBottomNavigation(
    navController: NavController
) {
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        listOf(
            BottomNavItem.Home,
            BottomNavItem.Library,
            BottomNavItem.Studio,
            BottomNavItem.Profile
        ).forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
