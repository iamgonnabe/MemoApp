package com.example.memoapp.composable

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R
import com.example.memoapp.data.Memo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, isFolder: Boolean, folderId: Long,  viewModel: MemoViewModel, onBackNavClicked: () -> Unit = {}){

    val view = LocalView.current
    val context = LocalContext.current
    //val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val expanded = remember {
        mutableStateOf(false)
    }
    val items = listOf(
        "메모 선택" to painterResource(id = R.drawable.baseline_check_circle_outline_24),
        "이름 변경" to painterResource(id = R.drawable.baseline_mode_edit_outline_24)
    )
    val enabled = remember {
        mutableStateListOf(false, true)
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
                if(viewModel.memosInFolder>0) enabled[0] = true
                if(folderId == 1L) enabled[1] = false
                if(!viewModel.isMemoEditing){
                    IconButton(onClick = { expanded.value = true }) {
                        Icon(painterResource(id = R.drawable.baseline_menu_24), contentDescription = null)
                    }
                } else {
                    TextButton(onClick = {
                        viewModel.isMemoEditing = false
                    }) {
                        Text(text = "완료", fontSize = 18.sp, color = colorResource(id = R.color.iconTextColor))
                    }
                }
                DropdownMenu(
                    modifier = Modifier
                        .width(240.dp)
                        .background(color = colorResource(id = R.color.cardColor)),
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    items.forEachIndexed { index, (item, icon) ->
                        DropdownMenuItem(
                            enabled = enabled[index],
                            onClick = {
                                expanded.value = false
                                if (index == 0) {
                                    viewModel.isMemoEditing = true
                                } else {
                                    viewModel.changeFolderNameDialogOpenState = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Icon(
                                    painter = icon,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                        if (index < items.size - 1) {
                            Divider(
                                color = Color.Gray,
                                thickness = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
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
                if(viewModel.isNewMemo && viewModel.isMemoUpdating){
                    TextButton(onClick = {
                        viewModel.addMemo(memo = viewModel.memoState, folderId = folderId)
                        viewModel.incrementMemoCount(folderId = folderId)
                        viewModel.isMemoUpdating = false
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }) {
                        Text(text = "완료", fontSize = 18.sp, color = colorResource(id = R.color.iconTextColor))
                    }
                } else if(!viewModel.isNewMemo && viewModel.isMemoUpdating
                    ) {
                    TextButton(onClick = {
                        viewModel.updateMemo(Memo(id = viewModel.memoIdState, memo = viewModel.memoState, folderId = folderId))
                        viewModel.isMemoUpdating = false
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
}