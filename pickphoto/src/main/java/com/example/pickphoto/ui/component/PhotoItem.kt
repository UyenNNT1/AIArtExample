package com.example.pickphoto.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.pickphoto.R
import com.example.pickphoto.model.PhotoData

@Composable
fun PhotoItem(
    photo: PhotoData,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photo.uri.ifEmpty { photo.path })
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
                .offset((-6).dp, 6.dp)
        ) {
            if (isSelected) {
                Image(
                    painter = painterResource(id = R.drawable.ic_select_button),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.8f), CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoItemPreview() {
    PhotoItem(
        photo = PhotoData(
            id = "1",
            uri = "/storage/sdcard0/Pictures/.thumbnails/1000000020.jpg",
            path = "/storage/sdcard0/Pictures/.thumbnails/1000000020.jpg"
        ),
        isSelected = true,
        onClick = {},
    )
}