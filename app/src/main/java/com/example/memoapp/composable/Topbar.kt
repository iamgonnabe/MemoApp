package com.example.memoapp.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R
import com.example.memoapp.data.Memo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, isFolder: Boolean, folderId: Long,  viewModel: MemoViewModel, onBackNavClicked: () -> Unit = {},){

    val navigationIcon : @Composable () -> Unit =
        if(!title.contains("폴더")){
            {
                IconButton(onClick = { onBackNavClicked() }) {
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
                IconButton(onClick = { /* edit folder drawer */ }) {
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
                if(viewModel.memoState.isNotEmpty()){
                    TextButton(onClick = {
                        viewModel.addMemo(memo = viewModel.memoState, folderId = folderId)
                        onBackNavClicked()
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