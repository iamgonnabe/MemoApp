package com.example.memoapp.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.memoapp.MemoViewModel
import com.example.memoapp.data.Folder

@Composable
fun FolderPage(id: Long, viewModel: MemoViewModel){
    if (id != 0L){
        val folder = viewModel.getFolderById(id).collectAsState(initial = Folder(0L, "", 0))
        viewModel.folderState = folder.value.title
    }else{
        viewModel.folderState = ""
    }
    Scaffold (
        topBar = {TopBar(title = viewModel.folderState, isNew = false )},
        containerColor = Color.Black
    ){
        Text(text = "dsds", modifier = Modifier.padding(it))
    }
}