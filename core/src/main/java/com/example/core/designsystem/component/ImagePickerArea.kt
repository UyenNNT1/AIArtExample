package com.example.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.example.core.R
import com.example.core.designsystem.style.AppColors
import com.example.core.designsystem.style.LocalCustomTypography
import com.example.core.designsystem.style.pxToDp

@Composable
fun ImagePickerArea(
    modifier: Modifier = Modifier,
    selectedImageUrl: String? = null,
    onOpenPickPhoto: () -> Unit,
    aspectRatio: Float = 1f
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clip(RoundedCornerShape(12.pxToDp()))
            .border(
                width = 2.pxToDp(),
                color = AppColors.Main,
                shape = RoundedCornerShape(12.pxToDp())
            )
            .background(Color.White)
            .clickable { onOpenPickPhoto() }
    ) {
        if (selectedImageUrl != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = selectedImageUrl,
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_open_pick_photo),
                    contentDescription = "open pick photo",
                    modifier = Modifier
                        .size(40.pxToDp())
                        .padding(top = 16.pxToDp(), start = 16.pxToDp())
                        .clickable { onOpenPickPhoto() }
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_gallery),
                    contentDescription = "gallery icon",
                    modifier = Modifier.size(80.pxToDp()).clickable { onOpenPickPhoto() }
                )
                
                Spacer(modifier = Modifier.height(12.pxToDp()))
                
                AperoTextView(
                    text = "Add your photo",
                    textStyle = LocalCustomTypography.current.Title3.regular,
                    maxLines = 1,
                    modifier = Modifier,
                    marqueeEnabled = false
                )
            }
        }
    }
}

@Preview
@Composable
private fun ImagePickerAreaPreview() {
    ImagePickerArea(
        modifier = Modifier,
        selectedImageUrl = "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/Neon_City_After.jpg",
        onOpenPickPhoto = {},
        aspectRatio = 1f
    )
}