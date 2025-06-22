package com.example.aiartexample.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.aiartservice.network.model.AiArtParams
import com.example.core.designsystem.component.CoreInputTextField
import com.example.core.designsystem.component.GradientButton
import com.example.core.designsystem.component.ImagePickerArea
import com.example.core.designsystem.style.pxToDp
import com.example.aiartexample.R
import com.example.core.designsystem.component.BannerMessage

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onOpenPickPhoto: () -> Unit = {},
    onOpenResultScreen: () -> Unit = {},
    aiStyleViewModel: AiArtStyleViewModel
) {
    val styleUiState by aiStyleViewModel.uiState.collectAsState()

    val generatedImageState = styleUiState.generatedImageState

    LaunchedEffect(generatedImageState) {
        if (generatedImageState is UiState.Success) {
            onOpenResultScreen()
            aiStyleViewModel.resetGeneratedImageState()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
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
                selectedImageModel = styleUiState.originalImage,
                isVisibleButtonSwitch = true,
                onOpenPickPhoto = { onOpenPickPhoto() },
                aspectRatio = 1f
            )
            Spacer(modifier = Modifier.height(28.pxToDp()))
            when (val state = styleUiState.categoriesState) {
                is UiState.Loading -> {
                    /*no-op*/
                }

                is UiState.Success -> {
                    val categories = state.data
                    ChooseStyleScreen(
                        modifier = Modifier.height(162.pxToDp()),
                        categories = categories,
                        selectedCategoryIndex = styleUiState.currentCategoryIndex,
                        styles = categories[styleUiState.currentCategoryIndex].aiArtStyles,
                        selectedStyleIndex = styleUiState.currentStyleIndex,
                        onStyleClick = { aiStyleViewModel.updateStyleIndex(it) },
                        onCategoryClick = { aiStyleViewModel.updateCategoryIndex(it) }
                    )
                    Spacer(modifier = Modifier.height(28.pxToDp()))
                    GradientButton(
                        text = "Generate AI",
                        modifier = Modifier,
                        isSelected = styleUiState.currentStyleIndex != -1 && styleUiState.originalImage != null,
                        onClick = {
                            aiStyleViewModel.genAiArtImage(
                                AiArtParams(
                                    positivePrompt = "",
                                    styleId = categories[styleUiState.currentCategoryIndex].aiArtStyles[styleUiState.currentStyleIndex].id,
                                    pathImageOrigin = styleUiState.originalImage!!
                                )
                            )
                            onOpenResultScreen
                        }
                    )
                }

                else -> Unit
            }
            Spacer(modifier = Modifier.height(20.pxToDp()))
            BannerMessage(
                modifier = Modifier,
                message = "Select a style to generate AI art",
                isVisible = generatedImageState is UiState.Error
            )
        }
        FullscreenLoadingOverlay(isLoading = generatedImageState is UiState.Loading)
    }
}

@Composable
fun FullscreenLoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_generating)).value,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(150.pxToDp())
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {

}