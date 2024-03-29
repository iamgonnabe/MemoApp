package com.example.memoapp.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    var folderTitle by remember {
        mutableStateOf("")
    }
    folderTitle = folder.value.title

    val allMemos = viewModel.getAllMemosByFolderId(folderId).collectAsState(initial = listOf())

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
                    if(!viewModel.isMemoEditing){
                        Text(text = "")
                        Text(text = if(viewModel.memosInFolder>0)"${viewModel.memosInFolder}개의 메모" else "메모 없음",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 48.dp, top = 16.dp))
                        IconButton(onClick = {
                            navController.navigate(Screen.MemoScreen.route + "/${folderId}/0L")
                            viewModel.isNewMemo = true
                        }) {
                            Icon(
                                painterResource(id = R.drawable.outline_new_window_24),
                                contentDescription = null,
                                tint = colorResource(id = R.color.iconTextColor)
                            )
                        }
                    }else{
                        if(!viewModel.memoSelectedListState.contains(true)){
                            TextButton(onClick = { /*TODO*/ }) {
                                Text(text = "모두 이동", fontSize = 16.sp, color = colorResource(id = R.color.iconTextColor))
                            }
                            TextButton(onClick = {
                                viewModel.deleteMemos(allMemos.value)
                                viewModel.isMemoEditing = false
                                viewModel.memosInFolder = 0
                                viewModel.updateFolder(folder = Folder(id = folderId, title = viewModel.folderState, memoCount = 0))
                            }) {
                                Text(text = "모두 삭제", fontSize = 16.sp, color = colorResource(id = R.color.iconTextColor))
                            }
                        }else{
                            val memoCount = viewModel.memoSelectedListState.count{ it }
                            TextButton(onClick = { /*TODO*/ }) {
                                Text(text = "이동", fontSize = 16.sp, color = colorResource(id = R.color.iconTextColor))
                            }
                            TextButton(onClick = {
                                viewModel.deleteMemos(viewModel.deleteMemoList)
                                viewModel.isMemoEditing = false
                                viewModel.memoSelectedListState.clear()
                                viewModel.decrementMemoCount(folderId, memoCount)
                            }) {
                                Text(text = "삭제", fontSize = 16.sp, color = colorResource(id = R.color.iconTextColor))
                            }
                        }
                    }
                }
            }
        )
    }
    
    Scaffold (
        topBar = {TopBar(title = folderTitle, isFolder = isFolder, folderId = folderId, viewModel = viewModel){
            navController.navigateUp()
            viewModel.isMemoEditing = false
        } },
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
            Box(modifier = Modifier.padding(20.dp))
            LazyColumn {
                items(memoList.value, key = { memo -> memo.id }) { memo ->
                    MemoItem(viewModel = viewModel, memo = memo) {
                        navController.navigate(Screen.MemoScreen.route + "/${folderId}/${memo.id}")
                    }
                }
            }
        }
        ChangeFolderNameDialog(viewModel=viewModel, folderId = folderId)
    }
}

@Composable
fun MemoItem(viewModel: MemoViewModel, memo: Memo, onClick: ()-> Unit){
    val title = memo.memo.split("\n").firstOrNull()
    val isChecked = remember {
        mutableStateOf(false)
    }
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 16.dp)
        .clickable {
            onClick()
            viewModel.isNewMemo = false
        }, elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.cardColor))) {
        Row (modifier = Modifier.padding(start = 20.dp, top = 4.dp)){
            if(viewModel.isMemoEditing){
                IconButton(onClick = {
                    if(!isChecked.value){
                        isChecked.value = true
                        viewModel.memoSelectedListState.add(true)
                        viewModel.deleteMemoList.add(memo)
                    }else{
                     isChecked.value = false
                     viewModel.memoSelectedListState.remove(true)
                        viewModel.deleteMemoList.remove(memo)
                    }
                }) {
                    if(isChecked.value) Icon(painter = painterResource(id = R.drawable.baseline_check_circle_24), tint = colorResource(
                        id = R.color.iconTextColor), contentDescription = null)
                    else Icon(painter = painterResource(id = R.drawable.outline_circle_24), tint = Color.Gray, contentDescription = null)
                }
            }
            Column {
                Text(text = title!!, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                    color = Color.White, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Row (modifier = Modifier.fillMaxWidth()){
                    Text(text = memo.time, color = colorResource(id = R.color.memoTextColor), fontSize = 18.sp)
                    Box(modifier = Modifier.padding(4.dp))
                    Text(text = memo.memo.split("\n").getOrNull(1) ?: "추가 텍스트 없음", color = colorResource(
                        id = R.color.memoTextColor), overflow = TextOverflow.Ellipsis, maxLines = 1, fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ChangeFolderNameDialog(viewModel: MemoViewModel, folderId: Long){
    if(viewModel.changeFolderNameDialogOpenState){
        Dialog(onDismissRequest = { viewModel.changeFolderNameDialogOpenState = false },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Card(
                modifier = Modifier
                    .height(160.dp)
                    .width(280.dp)
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.cardColor)
                )
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "폴더 이름 변경", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
                    OutlinedTextField(
                        value = viewModel.folderState,
                        onValueChange = {viewModel.onFolderChanged(it)},
                        placeholder = { Text(text = "이름", color = Color.White)},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .padding(horizontal = 8.dp),
                        singleLine = true,
                        textStyle = TextStyle.Default.copy(fontSize = 4.sp),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {IconButton(onClick = { viewModel.folderState = "" }) {
                            Icon(painter = painterResource(id = R.drawable.baseline_clear_24), contentDescription = null, tint = Color.Gray)
                        }},
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colorResource(id = R.color.bgColor),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            unfocusedContainerColor = colorResource(id = R.color.bgColor),
                            cursorColor = colorResource(id = R.color.iconTextColor),
                            focusedBorderColor = Color.Gray)
                    )
                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceEvenly){
                        TextButton(onClick = { viewModel.changeFolderNameDialogOpenState = false } ) {
                            Text(text = "취소", fontSize = 18.sp, color= colorResource(id = R.color.iconTextColor))
                        }
                        TextButton(
                            enabled = viewModel.folderState.isNotEmpty(),
                            onClick = {
                                viewModel.changeFolderNameDialogOpenState = false
                            viewModel.updateFolder(Folder(id = folderId, title = viewModel.folderState, memoCount = viewModel.memosInFolder ))
                        } ) {
                            Text(text = "저장",
                                fontSize = 18.sp,
                                color= if(viewModel.folderState.isNotEmpty()) colorResource(id = R.color.iconTextColor) else Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}

