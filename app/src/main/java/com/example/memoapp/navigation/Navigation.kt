package com.example.memoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel
import com.example.memoapp.composable.FolderPage
import com.example.memoapp.composable.MainPage

@Composable
fun Navigation(viewModel: MemoViewModel = viewModel(), navController: NavController){
    NavHost(navController = navController as NavHostController, startDestination = Screen.MainScreen.HomeScreen.route){
        composable(Screen.MainScreen.HomeScreen.route){
            MainPage(navController = navController)
        }
        composable(
            Screen.MainScreen.FolderScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                })
        ){
                entry->
            val id = entry.arguments?.getLong("id") ?: 0L
            FolderPage(id= id, viewModel = viewModel)
        }

        composable(Screen.MainScreen.MemoScreen.route){
            //memoPage
        }
    }
}