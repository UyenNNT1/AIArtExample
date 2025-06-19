package com.example.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.designsystem.style.LocalCustomColors
import com.example.core.designsystem.style.LocalCustomTypography
import com.example.core.designsystem.style.pxToDp

@Composable
fun GradientButton(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val gradient = if (isSelected) {
        Brush.horizontalGradient(
            colors = listOf(
                LocalCustomColors.current.material.primary,
                LocalCustomColors.current.material.secondary
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFEAA1E8),
                Color(0xFFA095E8)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16))
            .background(brush = gradient)
            .clickable { onClick() }
            .padding(vertical = 12.pxToDp(), horizontal = 32.pxToDp()),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = LocalCustomTypography.current.Title3.medium,
            color = LocalCustomColors.current.material.surface
        )
    }
}

@Preview(showBackground = false, name = "button selected")
@Composable
private fun ButtonPreview() {
    GradientButton(
        text = "Generate AI",
        modifier = Modifier,
        isSelected = true,
        onClick = {}
    )
}

@Preview(showBackground = false, name = "button unselected")
@Composable
private fun ButtonUnSelectedPreview() {
    GradientButton(
        text = "Generate AI",
        modifier = Modifier,
        isSelected = false,
        onClick = {}
    )
}

