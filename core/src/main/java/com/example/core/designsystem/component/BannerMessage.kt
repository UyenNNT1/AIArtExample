package com.example.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.designsystem.style.LocalCustomTypography
import com.example.core.designsystem.style.pxToDp

@Composable
fun BannerMessage(
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    message: String = ""
) {
    if (!isVisible) return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFB71C1C))
            .padding(vertical = 12.pxToDp(), horizontal = 16.pxToDp())
    ) {
        Text(
            text = message,
            color = Color.White,
            style = LocalCustomTypography.current.Body.medium
        )
    }
}

@Preview
@Composable
private fun BannerMessagePreview() {
    BannerMessage(message = "No internet connection! Try again later")
}
