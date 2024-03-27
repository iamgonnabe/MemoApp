package com.example.memoapp.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R
import com.example.memoapp.data.Memo


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MemoPage(folderId: Long, memoId: Long, viewModel: MemoViewModel, navController: NavController){

    if(memoId != 0L){
        val memo = viewModel.getMemoById(memoId).collectAsState(initial = Memo(0L,"","",0L))
        viewModel.memoState = memo.value.memo
        viewModel.memoIdState = memo.value.id
    } else {
        viewModel.memoState = ""
    }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            TopBar(title = "", isFolder = false, folderId = folderId, viewModel = viewModel){
                navController.navigateUp()
                viewModel.isMemoUpdating = false
            }
        },
        bottomBar = {BottomBar(viewModel = viewModel)},
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextField(
                value = viewModel.memoState,
                onValueChange = { viewModel.onMemoChanged(it)
                    viewModel.isMemoUpdating = true},
                modifier = Modifier
                    .fillMaxSize(),
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

}