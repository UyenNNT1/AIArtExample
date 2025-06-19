package com.example.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.style.AppColors
import com.example.core.designsystem.style.pxToDp
import com.example.core.R

@Composable
fun AIInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Enter your prompt...",
    isEnabled: Boolean = true,
    onClearClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.pxToDp())
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = AppColors.Main,
                shape = RoundedCornerShape(12.dp)
            )
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = isEnabled,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Clear",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AllInputFieldPreview() {
    AIInputField(
        value = "",
        onValueChange = {},
        onClearClick = {}
    )
}