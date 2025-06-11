package com.example.core.designsystem.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.style.LocalCustomTypography
import com.example.core.designsystem.style.pxToDp

@Composable
fun AperoTextView(
    text: String,
    textStyle: TextStyle,
    maxLines: Int? = null,
    modifier: Modifier = Modifier,
    marqueeEnabled: Boolean = false
) {
    Box(
        modifier = modifier
            .height(
                textStyle.lineHeight.value.pxToDp() *
                        (maxLines ?: (text.count { it == '\n' } + 1))
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = textStyle,
            maxLines = maxLines ?: Int.MAX_VALUE,
            overflow = if (maxLines != null) TextOverflow.Ellipsis else TextOverflow.Clip,
            modifier = if (marqueeEnabled) {
                Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE,
                )
            } else {
                Modifier
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AperoTextViewPreview() {
    Column {
        // Preview with 2 lines max
        AperoTextView(
            text = "This is a very long text that will definitely overflow and show ellipsis when it reaches more than two lines because it's too long to fit",
            textStyle = LocalCustomTypography.current.Headline.semiBold,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preview with unlimited lines
        AperoTextView(
            text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5",
            textStyle = LocalCustomTypography.current.Headline.semiBold
        )
    }
}