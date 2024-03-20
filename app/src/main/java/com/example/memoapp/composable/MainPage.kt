package com.example.memoapp.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R
import com.example.memoapp.ScreenViewModel
import com.example.memoapp.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainPage(navController: NavController){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val scope: CoroutineScope = rememberCoroutineScope()
    val screenViewModel: ScreenViewModel = viewModel()
    val memoViewModel: MemoViewModel = viewModel()
    val isSheetFullScreen by remember{ mutableStateOf(false) }
    val modifier = if(isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth().fillMaxHeight()

    val currentScreen = remember {
        screenViewModel.currentScreen.value
    }

    val title = remember{
        mutableStateOf(currentScreen.title)
    }

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = {it != ModalBottomSheetValue.Expanded}
    )

    val roundedCornerRadius = if(isSheetFullScreen) 0.dp else 12.dp
    val bottomBar: @Composable ()-> Unit = {
        BottomAppBar(
            containerColor = Color.DarkGray, // 배경색 변경
            contentColor = colorResource(id = R.color.contentColor),
            actions = {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
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
                    }) {
                        Icon(painterResource(id = R.drawable.outline_create_new_folder_24), contentDescription = null, modifier = Modifier.size(32.dp))
                    }
                    IconButton(onClick = {navController.navigate(Screen.MainScreen.MemoScreen.route)}) {
                        Icon(painterResource(id = R.drawable.outline_new_window_24), contentDescription = null, modifier = Modifier.size(32.dp))
                    }
                }
            }
        )
    }
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundedCornerRadius, topEnd = roundedCornerRadius),
        sheetContent = {
        MoreBottomSheet(modifier= modifier, viewModel = memoViewModel)
    }) {
        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            backgroundColor = Color.Black,
            topBar = {TopBar(title = title, isNew = false)},
            bottomBar = bottomBar
        ) {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp, start = 16.dp, end = 16.dp)
                .clickable { },
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.cardColor))
            ) {
                Column (modifier = Modifier.padding(16.dp)){
                    Text(text = "스껅", fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }
        }
    }



}

@Composable
fun MoreBottomSheet(modifier: Modifier, viewModel: MemoViewModel){
    Box(modifier = modifier
        .background(Color.DarkGray)){
        NewFolderTextField(value = viewModel.folderState, onValueChanged = {viewModel.onFolderChanged(it)})
    }
}

@Composable
fun NewFolderTextField(
    value: String,
    onValueChanged: (String)-> Unit
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        placeholder = { Text(text = "새로운 폴더", color = Color.White)},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colorResource(id = R.color.contentColor), focusedTextColor = Color.Black)
    )
}


