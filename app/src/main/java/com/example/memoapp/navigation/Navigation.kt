package com.example.memoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel
import com.example.memoapp.composable.FolderPage
import com.example.memoapp.composable.MainPage
import com.example.memoapp.composable.NewMemoPage
import com.example.memoapp.data.Folder

@Composable
fun Navigation(viewModel: MemoViewModel = viewModel(), navController: NavController){
    NavHost(navController = navController as NavHostController, startDestination = Screen.HomeScreen.route){
        composable(Screen.HomeScreen.route){
            MainPage(navController = navController)
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
            FolderPage(folderId = id, viewModel = viewModel, navController = navController)
        }

        composable(
            Screen.NewMemoScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = 0L
                    nullable = false
                })
        ){
                entry->
            val id = entry.arguments?.getLong("id") ?: 0L
            NewMemoPage(folderId = id, viewModel = viewModel, navController = navController)
        }


    }
}