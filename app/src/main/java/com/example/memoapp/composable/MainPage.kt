package com.example.memoapp.composable

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel

@Composable
fun MainPage(navController: NavController, viewModel: MemoViewModel){
    val context = LocalContext.current

}