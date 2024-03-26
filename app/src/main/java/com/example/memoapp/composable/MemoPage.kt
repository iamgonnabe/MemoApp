package com.example.memoapp.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R
import com.example.memoapp.data.Memo


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MemoPage(folderId: Long, memoId: Long, viewModel: MemoViewModel, navController: NavController){
    val focusManager = LocalFocusManager.current
    if(memoId != 0L){
        val memo = viewModel.getMemoById(memoId).collectAsState(initial = Memo(0L,"","",0L))
        viewModel.memoState = memo.value.memo
    } else {
        viewModel.memoState = ""
    }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            TopBar(title = "", isFolder = false, folderId = folderId, viewModel = viewModel){
                navController.navigateUp()
            }
        },
        bottomBar = {BottomBar(viewModel = viewModel)},
        scaffoldState = scaffoldState
    ) {
        TextField(
            value = viewModel.memoState,
            onValueChange = {viewModel.onMemoChanged(it)},
            modifier = Modifier
                .fillMaxSize(),
            maxLines = Int.MAX_VALUE,
            singleLine = false,
            keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()}),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
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