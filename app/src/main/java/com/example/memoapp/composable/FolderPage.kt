package com.example.memoapp.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R
import com.example.memoapp.data.Folder
import com.example.memoapp.data.Memo
import com.example.memoapp.navigation.Screen

@Composable
fun FolderPage(folderId: Long, viewModel: MemoViewModel, navController: NavController){
    val isFolder by remember {
        mutableStateOf(true)
    }
    val nestedScrollInterop = rememberNestedScrollInteropConnection()
    val scaffoldState = rememberScaffoldState()
    val folder = viewModel.getFolderById(folderId).collectAsState(initial = Folder(0L,"",0))
    viewModel.folderState = folder.value.title
    viewModel.memosInFolder = folder.value.memoCount

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
                    IconButton(onClick = {
                        navController.navigate(Screen.MemoScreen.route + "/${folderId}/0L")
                        viewModel.memoEditingState = true
                    }) {
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
        topBar = {TopBar(title = viewModel.folderState, isFolder = isFolder, folderId = folderId, viewModel = viewModel ){navController.navigateUp()} },
        bottomBar = bottomBar,
        backgroundColor = Color.Black,
        scaffoldState = scaffoldState
    ){
        val memoList = viewModel.getAllMemosByFolderId(folderId = folderId).collectAsState(initial = listOf())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .nestedScroll(nestedScrollInterop)
        ) {
            LazyColumn {
                items(memoList.value, key = { memo -> memo.id }) { memo ->
                    MemoItem(viewModel = viewModel, memo = memo) {
                        navController.navigate(Screen.MemoScreen.route + "/${folderId}/${memo.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun MemoItem(viewModel: MemoViewModel, memo: Memo, onClick: ()-> Unit){
    val title = memo.memo.split("\n").firstOrNull()
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 16.dp)
        .clickable {
            onClick()
        }, elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.cardColor))) {
        Column (
            modifier = Modifier.padding(horizontal = 12.dp)
        ){
            Text(text = title!!, fontSize = 26.sp, fontWeight = FontWeight.Bold,
                color = Color.White, overflow = TextOverflow.Clip, maxLines = 1)
            Row {
                Text(text = memo.time, color = colorResource(id = R.color.memoTextColor))
                Text(text = memo.memo.split("\n").getOrNull(1) ?: "추가 텍스트 없음", color = colorResource(
                    id = R.color.memoTextColor), overflow = TextOverflow.Clip, maxLines = 1
                )
            }
            Divider(color = colorResource(id = R.color.bgColor), thickness = 1.dp)
        }
    }
}