package com.example.aiartexample.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.aiartexample.model.AiArtUiState
import com.example.core.designsystem.component.CoreInputTextField
import com.example.core.designsystem.component.GradientButton
import com.example.core.designsystem.component.ImagePickerArea
import com.example.core.designsystem.style.pxToDp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    aiArtUiState: AiArtUiState = AiArtUiState(),
    onOpenPickPhoto: () -> Unit = {},
    aiArtViewModel: AiArtViewModel,
    aiStyleViewModel: AiArtStyleViewModel
) {
    val styleUiState by aiStyleViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                top = 28.pxToDp(),
                bottom = 28.pxToDp(),
                start = 24.pxToDp(),
                end = 24.pxToDp()
            )
    ) {
        CoreInputTextField(
            modifier = Modifier,
            value = "",
            onValueChange = {},
            placeholder = "Enter your prompt",
            isEnabled = true,
            onClearClick = {}
        )
        Spacer(modifier = Modifier.height(28.pxToDp()))
        ImagePickerArea(
            modifier = Modifier,
            selectedImageModel = null,
            onOpenPickPhoto = { onOpenPickPhoto() },
            aspectRatio = 1f
        )
        Spacer(modifier = Modifier.height(28.pxToDp()))
        ChooseStyleScreen(
            modifier = Modifier.height(162.pxToDp()),
            categories = emptyList(),
            selectedCategoryIndex = 0,
            styles = emptyList(),
            onStyleClick = {},
            onCategoryClick = {}
        )
        Spacer(modifier = Modifier.height(28.pxToDp()))
        GradientButton(
            text = "Generate AI",
            modifier = Modifier,
            isSelected = false,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {

}