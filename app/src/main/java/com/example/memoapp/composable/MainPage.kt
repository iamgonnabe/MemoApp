package com.example.memoapp.composable

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R
import com.example.memoapp.data.Folder
import com.example.memoapp.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainPage(navController: NavController, viewModel: MemoViewModel){
    val nestedScrollInterop = rememberNestedScrollInteropConnection()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val isSheetFullScreen by remember{ mutableStateOf(false) }

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    val roundedCornerRadius = if(isSheetFullScreen) 0.dp else 36.dp
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    IconButton(onClick = {
                        scope.launch {
                            if(modalSheetState.isVisible){
                                modalSheetState.hide()
                            }else{
                                modalSheetState.show()
                            }
                        }
                        viewModel.editFolderState = false
                    }) {
                        Icon(painterResource(id = R.drawable.outline_create_new_folder_24),
                            contentDescription = null,
                            tint = colorResource(id = R.color.iconTextColor)
                        )
                    }
                    IconButton(onClick = {
                        viewModel.isNewMemo = true
                        navController.navigate(Screen.MemoScreen.route + "/1L/0L")
                        viewModel.editFolderState = false
                    }) {
                        Icon(painterResource(id = R.drawable.outline_new_window_24),
                            contentDescription = null,
                            tint = colorResource(id = R.color.iconTextColor)
                        )
                    }
                }
            }
        )
    }
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = {
        MoreBottomSheet(viewModel = viewModel, modalSheetState = modalSheetState)
    }) {
        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color.Black,
            topBar = {TopBar(title = "폴더", isFolder = false, folderId = 0L, viewModel = viewModel)},
            bottomBar = bottomBar
        ) {
            val folderList = viewModel.getAllFolders.collectAsState(initial = listOf())
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollInterop)
            ) {
                Text(
                    text = "나의 Phone",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)
                )
                LazyColumn {
                    items(folderList.value, key = { folder -> folder.id }) { folder ->
                        FolderItem(folder = folder, viewModel = viewModel) {
                            if(!viewModel.editFolderState){
                                navController.navigate(Screen.FolderScreen.route + "/${folder.id}")
                            }
                        }
                    }
                }
            }

        }
    }



}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MoreBottomSheet(viewModel: MemoViewModel, modalSheetState: ModalBottomSheetState){
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(colorResource(id = R.color.bgColor))
        .padding(top = 16.dp))
    {
        val dialogOpen = remember {
            mutableStateOf(false)
        }
        NewFolderTextField(viewModel = viewModel, modalSheetState = modalSheetState, dialogOpen = dialogOpen)
        AlertDialog(dialogOpen = dialogOpen)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewFolderTextField(
    viewModel: MemoViewModel,
    modalSheetState: ModalBottomSheetState,
    dialogOpen: MutableState<Boolean>
){
    val view = LocalView.current
    val context = LocalContext.current
    val scope: CoroutineScope = rememberCoroutineScope()
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            TextButton(onClick = {
                scope.launch {
                    modalSheetState.hide()
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                viewModel.folderState = ""
            }) {
                Text(text = "취소", fontSize = 18.sp, color = colorResource(id = R.color.iconTextColor))
            }
            Text(text = "새로운 폴더", fontSize = 20.sp, color= Color.White)
            TextButton(onClick = {
                if(viewModel.folderState.isNotEmpty()){
                    viewModel.addFolder(
                        Folder(
                            title = viewModel.folderState
                        )
                    )
                    viewModel.folderState = ""
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    scope.launch{
                        modalSheetState.hide()
                    }
                }else{
                    dialogOpen.value = true
                }
            }) {
                Text(text = "완료", fontSize = 18.sp, color = colorResource(id = R.color.iconTextColor))
            }
        }
        OutlinedTextField(
            value = viewModel.folderState,
            onValueChange = {viewModel.onFolderChanged(it)},
            placeholder = { Text(text = "새로운 폴더", color = Color.White)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onDone = {
                softwareKeyboardController?.hide()
            }),
            trailingIcon = {IconButton(onClick = { viewModel.folderState = "" }) {
                Icon(painter = painterResource(id = R.drawable.baseline_clear_24), contentDescription = null)
            }},
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colorResource(id = R.color.cardColor),
                focusedTextColor = Color.White, unfocusedContainerColor = colorResource(id = R.color.cardColor), cursorColor = colorResource(
                    id = R.color.iconTextColor), focusedBorderColor = colorResource(id = R.color.cardColor))
        )
    }
}

@Composable
fun FolderItem(folder: Folder, viewModel:MemoViewModel, onClick: () -> Unit){
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .padding(horizontal = 4.dp)
        .clickable {
            onClick()
            Log.d("folderId", "${folder.id}")
        }, elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.cardColor))) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Row (verticalAlignment = Alignment.CenterVertically){
                Icon(painterResource(id = R.drawable.outline_folder_open_24),
                    tint = colorResource(id = R.color.iconTextColor),
                    modifier = Modifier.size(26.dp),
                    contentDescription = null)
                Box(modifier = Modifier.size(10.dp))
                Text(text = folder.title, fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Medium)
            }
            if(viewModel.editFolderState /* && !folder.title.contains("메모")*/){
                IconButton(onClick = {
                    viewModel.deleteFolder(folder)
                }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_menu_24),
                        contentDescription = null
                    )
                }
            }else{
                Row (verticalAlignment = Alignment.CenterVertically){
                    Text(text = folder.memoCount.toString(), fontSize = 22.sp, color = Color.Gray)
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(26.dp))

                }
            }
        }
    }
}

@Composable
fun AlertDialog(dialogOpen: MutableState<Boolean>){
    if(dialogOpen.value){
        Dialog(onDismissRequest = { dialogOpen.value = false },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
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
                    Text(text = "다른 이름을 선택하십시오.", fontSize = 18.sp, color= Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
                    Text(text = "폴더 이름을 공백으로 남겨둘 수 없습니다.", fontSize = 14.sp, color = Color.White, modifier = Modifier.padding(bottom = 16.dp))
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                    TextButton(onClick = { dialogOpen.value = false } ) {
                        Text(text = "확인", fontSize = 18.sp, color= colorResource(id = R.color.iconTextColor))
                    }
                }
            }
        }
    }
}

