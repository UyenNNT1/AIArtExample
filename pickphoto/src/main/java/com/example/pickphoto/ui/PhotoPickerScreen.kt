package com.example.pickphoto.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pickphoto.model.PhotoData
import com.example.pickphoto.ui.component.NavigationBarCustom
import com.example.pickphoto.ui.component.PhotoItem

@Composable
fun PhotoPickerScreen(
    photos: List<PhotoData> = emptyList(),
    selectedPhoto: PhotoData? = null,
    onCloseClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onPhotoClick: (PhotoData) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        NavigationBarCustom(
            selectedCount = if (selectedPhoto != null) 1 else 0,
            onCloseClick = onCloseClick,
            onNextClick = onNextClick,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                contentPadding = PaddingValues(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(photos) { photo ->
                    PhotoItem(
                        photo = photo,
                        isSelected = selectedPhoto?.id == photo.id,
                        onClick = { onPhotoClick(photo) }
                    )
                }
            }
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

    PhotoPickerScreen(
        photos = samplePhotos,
        selectedPhoto = samplePhotos[1]
    )
}