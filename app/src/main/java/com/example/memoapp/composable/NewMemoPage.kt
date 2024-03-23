package com.example.memoapp.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R
import com.example.memoapp.data.Folder


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewMemoPage(folderId: Long, viewModel: MemoViewModel, navController: NavController){

    viewModel.memoState = ""


    val isFolder by remember {
        mutableStateOf(false)
    }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            TopBar(title = "", isFolder = isFolder, folderId = folderId, viewModel = viewModel){
                navController.navigateUp()
            }
        }, scaffoldState = scaffoldState
    ) {
        TextField(
            value = viewModel.memoState,
            onValueChange = {viewModel.onMemoChanged(it)},
            modifier = Modifier
                .fillMaxSize(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /* Handle 'Done' action */ }),
            maxLines = Int.MAX_VALUE,
            singleLine = false,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Black,
                unfocusedContainerColor = Color.Black,
                cursorColor = colorResource(id = R.color.iconTextColor)
            )
        )
    }

}