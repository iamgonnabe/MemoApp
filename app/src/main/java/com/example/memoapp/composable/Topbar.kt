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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.memoapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: MutableState<String>, onBackNavClicked: () -> Unit = {}, isNew : Boolean){

    val navigationIcon : @Composable () -> Unit =
        if(!title.value.contains("폴더")){
            {
                IconButton(onClick = { onBackNavClicked() }) {
                    Icon(painterResource(id = R.drawable.baseline_arrow_back_ios_24), contentDescription = null)
                }
            }
        }else {
            if(title.value.contains("새로운 폴더") && isNew){
                {
                    TextButton(onClick = {onBackNavClicked()}) {
                        Text(text = "취소", fontSize = 18.sp, color = colorResource(id = R.color.contentColor))
                    }
                }
            }else{
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
        }
    val actionTextButton : @Composable RowScope.() -> Unit =
        if(title.value.contains("폴더")){
            {
                TextButton(onClick = { /* 새로 생성한 폴더 메뉴 버튼 생김 그리고 텍스트가 완료로 바뀜 */ }) {
                Text(text = "편집", fontSize = 18.sp, color = colorResource(id = R.color.contentColor))
                }
            }
        }else if(title.value.contains("메모")) {
            {
                IconButton(onClick = { /* edit folder drawer */ }) {
                    Icon(painterResource(id = R.drawable.baseline_menu_24), contentDescription = null)
                }
            }
        }else{
            {
                IconButton(onClick = { /* share */ }) {
                    Icon(painterResource(id = R.drawable.baseline_ios_share_24), contentDescription = null)
                }
                IconButton(onClick = { /* edit memo drawer*/ }) {
                    Icon(painterResource(id = R.drawable.baseline_menu_24), contentDescription = null)
                }

            }
        }
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black,
            titleContentColor = Color.White,
            navigationIconContentColor = colorResource(id = R.color.contentColor),
            actionIconContentColor = colorResource(id = R.color.contentColor)
        ),
        title = {
            Text(
                title.value,
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