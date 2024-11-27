package com.soundsync.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Library : Screen("library")
    object Studio : Screen("studio")
    object Profile : Screen("profile")
    object Player : Screen("player")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object PlaylistDetail : Screen("playlist/{playlistId}") {
        fun createRoute(playlistId: String) = "playlist/$playlistId"
    }
    object ArtistDetail : Screen("artist/{artistId}") {
        fun createRoute(artistId: String) = "artist/$artistId"
    }
    object AlbumDetail : Screen("album/{albumId}") {
        fun createRoute(albumId: String) = "album/$albumId"
    }
}
