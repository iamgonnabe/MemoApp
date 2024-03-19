package com.example.memoapp.navigation

sealed class Screen(val route: String) {
    data object HomeScreen: Screen(
        "home_screen"
    )
    data object FolderScreen: Screen(
        "folder_screen",
    )

}