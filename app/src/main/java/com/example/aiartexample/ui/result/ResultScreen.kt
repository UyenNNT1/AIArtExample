package com.example.aiartexample.ui.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.aiartexample.ui.home.FullscreenLoadingOverlay
import com.example.core.R
import com.example.core.designsystem.component.GradientButton
import com.example.core.designsystem.component.ImagePickerArea
import com.example.core.designsystem.style.pxToDp

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    resultViewModel: ResultViewModel,
    onBackClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {}
) {
    val uiState = resultViewModel.uiState.collectAsState()
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(
                    top = 28.pxToDp(),
                    bottom = 82.pxToDp()
                )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .size(40.pxToDp())
                    .clickable { onBackClick() }
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 24.pxToDp(),
                        end = 24.pxToDp()
                    )
            ) {
                ImagePickerArea(
                    modifier = Modifier.fillMaxSize(),
                    selectedImageModel = uiState.value.imageResult,
                    onOpenPickPhoto = { /*no-op*/ },
                    aspectRatio = 1f
                )
            }
            GradientButton(
                text = "Download Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.pxToDp(),
                        end = 24.pxToDp()
                    ),
                isSelected = true,
                onClick = { onDownloadClick() }
            )
        }
        FullscreenLoadingOverlay(isLoading = false)
    }
    
}

@Preview(showBackground = true)
@Composable
private fun ResultScreenPreview() {

}