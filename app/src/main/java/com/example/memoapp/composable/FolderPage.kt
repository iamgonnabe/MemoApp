package com.example.memoapp.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R
import com.example.memoapp.data.Folder
import com.example.memoapp.navigation.Screen

@Composable
fun FolderPage(id: Long, viewModel: MemoViewModel, navController: NavController){
    val isFolder by remember {
        mutableStateOf(true)
    }

    val scaffoldState = rememberScaffoldState()

    if (id != 0L){
        val folder = viewModel.getFolderById(id).collectAsState(initial = Folder(0L, "", 0))
        viewModel.folderState = folder.value.title
    }else{
        viewModel.folderState = "메모"
    }

    val bottomBar: @Composable ()-> Unit = {
        BottomAppBar(
            modifier = Modifier.wrapContentSize(),
            containerColor = Color.Black,
            tonalElevation = 16.dp,
            windowInsets = BottomAppBarDefaults.windowInsets,
            actions = {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "")
                    Text(text = if(viewModel.memosInFolder>0)"${viewModel.memosInFolder}개의 메모" else "메모 없음",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 48.dp, top = 16.dp))
                    IconButton(onClick = {navController.navigate(Screen.NewMemoScreen.route + "/${id}")}) {
                        Icon(
                            painterResource(id = R.drawable.outline_new_window_24),
                            contentDescription = null,
                            tint = colorResource(id = R.color.iconTextColor)
                        )
                    }
                }
            }
        )
    }
    
    Scaffold (
        topBar = {TopBar(title = viewModel.folderState, isFolder = isFolder, folderId = id, viewModel = viewModel ){navController.navigateUp()} },
        bottomBar = bottomBar,
        backgroundColor = Color.Black,
        scaffoldState = scaffoldState
    ){
        Text(text = "dsds", modifier = Modifier.padding(it))
    }
}