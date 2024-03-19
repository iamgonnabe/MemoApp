package com.example.memoapp.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.memoapp.R



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun MainPage(){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Color.Black,
        topBar = {TopBar(scrollBehavior = scrollBehavior, title = "폴더", isNew = false)},
        bottomBar = { BottomNavigationBar(navController = navController)}
    ) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp, start = 16.dp, end = 16.dp)
            .clickable { },
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.cardColor))
            ) {
            Column (modifier = Modifier.padding(16.dp)){
                Text(text = "ddsds", fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
        }
    }
}


