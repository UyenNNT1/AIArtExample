package com.example.pickphoto.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pickphoto.R

@Composable
fun NavigationBarCustom(
    selectedCount: Int,
    onCloseClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp)
                .clickable { onCloseClick() },
            tint = Color.Unspecified
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = stringResource(id = R.string.pick_photo_next),
            fontSize = 16.sp,
            color = if (selectedCount > 0) Color.Blue else Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 70.dp).clickable { onNextClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationBarPreview() {
    NavigationBarCustom(
        selectedCount = 1,
        onCloseClick = {},
        onNextClick = {}
    )
}