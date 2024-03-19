package com.example.memoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.memoapp.MemoViewModel
import com.example.memoapp.composable.FolderPage
import com.example.memoapp.composable.MainPage
import com.example.memoapp.composable.MemoPage
import com.example.memoapp.composable.NewFolder

@Composable
fun Navigation(viewModel: MemoViewModel = viewModel(), navController: NavHostController = rememberNavController()){
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route){
        composable(Screen.HomeScreen.route){
            MainPage()
        }
        composable(
            Screen.FolderScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                })
        ){
                entry->
            val id = entry.arguments?.getLong("id") ?: 0L
            FolderPage()
        }
    }
}