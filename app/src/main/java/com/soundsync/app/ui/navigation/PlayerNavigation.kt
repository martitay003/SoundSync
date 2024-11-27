package com.soundsync.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.soundsync.app.ui.player.MiniPlayer
import com.soundsync.app.ui.player.PlayerScreen
import com.soundsync.app.ui.player.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerNavigation(
    navController: NavHostController,
    viewModel: PlayerViewModel,
    modifier: Modifier = Modifier
) {
    var isPlayerExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Main content area
        Column(modifier = Modifier.fillMaxSize()) {
            // Your main app content goes here
            Box(modifier = Modifier.weight(1f)) {
                // NavHost or other content
            }

            // Mini Player at the bottom
            AnimatedVisibility(
                visible = !isPlayerExpanded,
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = fadeOut() + slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                MiniPlayer(
                    viewModel = viewModel,
                    onExpandPlayer = { isPlayerExpanded = true }
                )
            }
        }

        // Full screen player
        AnimatedVisibility(
            visible = isPlayerExpanded,
            enter = fadeIn() + slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = fadeOut() + slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                PlayerScreen(
                    viewModel = viewModel,
                    modifier = Modifier.systemBarsPadding()
                )
            }
        }
    }
}
