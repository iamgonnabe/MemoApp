package com.example.memoapp.composable


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.memoapp.MainActivity
import com.example.memoapp.MemoViewModel
import com.example.memoapp.R



@Composable
fun BottomBar(viewModel: MemoViewModel) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[READ_EXTERNAL_STORAGE] == true ||
            permissions[READ_MEDIA_VISUAL_USER_SELECTED] == true) {
            launcher.launch("image/*")
        } else {
            val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity,
                READ_EXTERNAL_STORAGE
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                context,
                READ_MEDIA_VISUAL_USER_SELECTED
            )

            if (rationaleRequired) {
                Toast.makeText(
                    context,
                    "Camera Permission is required for this feature",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Camera Permission is required. Please enable it in the Android Settings",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    BottomAppBar(
        modifier = Modifier.wrapContentSize(),
        containerColor = Color.Black,
        tonalElevation = 16.dp,
        windowInsets = BottomAppBarDefaults.windowInsets,
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_checklist_24),
                        tint = colorResource(id = R.color.iconTextColor),
                        contentDescription = null
                    )
                }
                IconButton(onClick = {
                    if (hasGalleryPermission(context)) {
                        Log.d("camera button","true")
                        launcher.launch("image/*")
                    } else {
                        Log.d("camera button", "false")
                        requestPermissionLauncher.launch(
                            arrayOf(
                                READ_MEDIA_VISUAL_USER_SELECTED,
                                READ_EXTERNAL_STORAGE
                            )
                        )
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_photo_camera_24),
                        tint = colorResource(id = R.color.iconTextColor),
                        contentDescription = null
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_draw_24),
                        tint = colorResource(id = R.color.iconTextColor),
                        contentDescription = null
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_new_window_24),
                        tint = colorResource(id = R.color.iconTextColor),
                        contentDescription = null
                    )
                }
                selectedImageUri?.let { uri ->
                    LoadImageFromUri(uri = uri)
                }
            }
        }
    )
}
@OptIn(ExperimentalCoilApi::class)
@Composable
fun LoadImageFromUri(uri: Uri) {
    val painter = rememberImagePainter(uri)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(200.dp),
    )
}



fun hasGalleryPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            context,
            READ_MEDIA_VISUAL_USER_SELECTED
        ) == PackageManager.PERMISSION_GRANTED
    }
}
