package com.example.aiartexample.ui.pickphoto

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.ui.component.NavigationBarCustom
import com.example.pickphoto.ui.component.PhotoItem
import com.example.pickphoto.utils.PermissionUtils

@Composable
fun PhotoPickerScreen(
    pickPhotoViewModel: PhotoPickerViewModel,
    onCloseClick: () -> Unit = {},
    onNextClick: (PhotoData) -> Unit = {},
) {
    val uiState = pickPhotoViewModel.uiState.collectAsState()

    var hasPermission by remember { mutableStateOf(false) }

    RequestStoragePermission {
        hasPermission = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        NavigationBarCustom(
            hasPhotoSelected = uiState.value.selectedPhoto != null,
            onCloseClick = onCloseClick,
            onNextClick = { onNextClick(uiState.value.selectedPhoto!!) },
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (hasPermission) {
                /*pickPhotoViewModel.loadAllPhotos()*/
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val photos = uiState.value.photos
                    items(photos) { photo ->
                        PhotoItem(
                            photo = photo,
                            isSelected = uiState.value.selectedPhoto?.id == photo.id,
                            onClick = { pickPhotoViewModel.updateSelectedPhoto(photo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RequestStoragePermission(
    onGranted: () -> Unit
) {
    val context = LocalContext.current
    val permission = PermissionUtils.getRequiredStoragePermission()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onGranted()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(permission)
        } else {
            onGranted()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoPickerScreenPreview() {
    val samplePhotos = List(12) { index ->
        PhotoData(
            id = "photo_$index",
            uri = "",
            path = ""
        )
    }
}