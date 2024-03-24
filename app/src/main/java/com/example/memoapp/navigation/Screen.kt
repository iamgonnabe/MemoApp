package com.example.memoapp.navigation

import com.example.memoapp.R

sealed class Screen(val route: String) {
    data object HomeScreen: Screen("home_screen")
    data object FolderScreen: Screen("folder_screen")
    data object MemoScreen: Screen("memo_screen")

}