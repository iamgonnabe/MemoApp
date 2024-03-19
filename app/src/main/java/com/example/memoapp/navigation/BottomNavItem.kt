package com.example.memoapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.memoapp.R

sealed class BottomNavItem(val route: String, val icon: Int) {
    data object MemoScreen: BottomNavItem(
        "memo_screen", R.drawable.outline_new_window_24
    )
    data object NewFolderScreen: BottomNavItem(
        "new_folder_screen", R.drawable.outline_create_new_folder_24
    )
}