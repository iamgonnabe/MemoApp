package com.example.memoapp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.memoapp.navigation.Screen

class ScreenViewModel: ViewModel() {

    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.MainScreen.HomeScreen)

    val currentScreen: MutableState<Screen> get() = _currentScreen
    fun setCurrentScreen(screen: Screen){
        _currentScreen.value = screen
    }
}