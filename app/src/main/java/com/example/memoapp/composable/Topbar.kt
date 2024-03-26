package com.example.memoapp.composable

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, isFolder: Boolean, folderId: Long,  viewModel: MemoViewModel, onBackNavClicked: () -> Unit = {}){

    val view = LocalView.current
    val context = LocalContext.current
    //val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val dialogOpen = remember {
        mutableStateOf(false)
    }

    val navigationIcon : @Composable () -> Unit =
        if(!title.contains("폴더")){
            {
                IconButton(onClick = {
                    onBackNavClicked()
                }) {
                    Icon(painterResource(id = R.drawable.baseline_arrow_back_ios_24), contentDescription = null)
                }
            }
        }else {
            {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.Transparent,
                        contentDescription = null
                    )
                }
            }
        }
    val actionTextButton : @Composable RowScope.() -> Unit =
        if(title.contains("폴더")){
            {
                TextButton(onClick = { viewModel.editFolderState = !viewModel.editFolderState}) {
                Text(text = if(viewModel.editFolderState) stringResource(id = R.string.complete) else stringResource(
                    id = R.string.edit
                ), fontSize = 18.sp, color = colorResource(id = R.color.iconTextColor))
                }
            }
        }else if(isFolder) {
            {
                IconButton(onClick = {
                    dialogOpen.value = true
                }) {
                    Icon(painterResource(id = R.drawable.baseline_menu_24), contentDescription = null)
                }
            }
        }else{
            {
                IconButton(onClick = { /* share */ }, enabled = viewModel.memoState.isNotEmpty()) {
                    Icon(painterResource(id = R.drawable.baseline_ios_share_24), contentDescription = null)
                }
                IconButton(onClick = { /* edit memo drawer*/ }) {
                    Icon(painterResource(id = R.drawable.baseline_menu_24), contentDescription = null)
                }
                if(viewModel.memoState.isNotEmpty() && viewModel.memoCreatingState){
                    TextButton(onClick = {
                        viewModel.addMemo(memo = viewModel.memoState, folderId = folderId)
                        viewModel.incrementMemoCount(folderId = folderId)
                        viewModel.memoCreatingState = false
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }) {
                        Text(text = "완료", fontSize = 18.sp, color = colorResource(id = R.color.iconTextColor))
                    }
                }

            }
        }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            navigationIconContentColor = colorResource(id = R.color.iconTextColor),
            actionIconContentColor = colorResource(id = R.color.iconTextColor)
        ),
        title = {
            Text(
                title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = navigationIcon,
        actions = actionTextButton,
    )
    MenuPopUp(dialogOpen, viewModel)
}

@Composable
fun MenuPopUp(dialogOpen: MutableState<Boolean>, viewModel: MemoViewModel){
    if(dialogOpen.value){
        Dialog(onDismissRequest = { dialogOpen.value = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                modifier = Modifier
                    .height(80.dp)
                    .width(200.dp)
                    .padding(8.dp)
                    .clickable (
                        enabled = viewModel.memosInFolder>0
                    ){
                        dialogOpen.value = false
                        viewModel.memoEditingState = true
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.cardColor)
                )
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = "메모 선택", color = Color.White)
                    Icon(painter = painterResource(id = R.drawable.baseline_check_circle_outline_24), tint = Color.White, contentDescription = null)
                }
            }
        }
    }
}

