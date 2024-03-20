package com.example.memoapp.navigation

import com.example.memoapp.R

sealed class Screen(val title: String, val route: String) {

    sealed class MainScreen(val bTitle: String, val bRoute: String) : Screen(bTitle, bRoute) {
        data object HomeScreen: Screen("폴더",
            "home_screen"
        )
        data object FolderScreen: Screen(
            "메모",
            "folder_screen",
        )
        data object MemoScreen: Screen(
            "",
            "memo_screen"
        )

    }
}