package com.example.aiartexample.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.core.R
import com.example.core.designsystem.component.AperoTextView
import com.example.core.designsystem.style.LocalCustomTypography
import com.example.core.designsystem.style.pxToDp

@Composable
fun ChooseStyleScreen(
    modifier: Modifier = Modifier,
    selectedTab: String = "Trending",
    onTabSelected: (String) -> Unit = {},
    styles: List<StyleItem> = sampleStyles
) {
    Column(modifier = modifier.padding(16.pxToDp())) {
        AperoTextView(
            text = "Choose your Style",
            textStyle = LocalCustomTypography.current.Title3.medium,
            modifier = Modifier.padding(bottom = 8.pxToDp()),
            maxLines = 1,
            marqueeEnabled = true
        )

        Spacer(modifier = Modifier.height(4.pxToDp()))

        StyleTabRow(
            tabs = listOf("Trending", "Fashion", "Anime", "Digital Art", "Painting"),
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )

        Spacer(modifier = Modifier.height(16.pxToDp()))

        StyleGrid(styles)
    }
}

@Composable
fun StyleTabRow(
    tabs: List<String>,
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = tabs.indexOf(selectedTab),
        edgePadding = 0.pxToDp(),
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(selectedTab)]),
                color = Color(0xFF9A3BEE)
            )
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTab == tab,
                onClick = { onTabSelected(tab) },
                selectedContentColor = Color(0xFF9A3BEE),
                unselectedContentColor = Color.Black
            ) {
                AperoTextView(
                    text = tab,
                    textStyle = LocalCustomTypography.current.Body.regular,
                    modifier = Modifier.padding(vertical = 8.pxToDp()),
                    maxLines = 1,
                    marqueeEnabled = true
                )
            }
        }
    }
}

@Composable
fun StyleCard(modifier: Modifier = Modifier, style: StyleItem, selected: Boolean = false) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(80.pxToDp())
            .wrapContentHeight()
    ) {
        AsyncImage(
            model = style.imageRes,
            contentDescription = null,
            modifier = Modifier
                .size(80.pxToDp())
                .clip(RoundedCornerShape(12.pxToDp()))
                .background(Color.Gray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.pxToDp()))
        AperoTextView(
            text = style.title,
            textStyle = LocalCustomTypography.current.Caption1.regular,
            modifier = Modifier.padding(top = 4.pxToDp()),
            maxLines = 1,
            marqueeEnabled = true
        )
    }
}

@Composable
fun StyleGrid(styles: List<StyleItem> , modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.pxToDp())
    ) {
        items(styles) { style ->
            StyleCard(style = style)
        }
    }
}

data class StyleItem(
    val imageRes: Int,
    val title: String
)

val sampleStyles = listOf(
    StyleItem(R.drawable.image_test, "Novelistic"),
    StyleItem(R.drawable.image_test, "Novelistic"),
    StyleItem(R.drawable.image_test, "Novelistic"),
    StyleItem(R.drawable.image_test, "Novelistic"),
    StyleItem(R.drawable.image_test, "Realistic")
)

@Preview(showBackground = true, name = "Choose Style Screen")
@Composable
fun PreviewChooseStyleScreen() {
    ChooseStyleScreen()
}

@Preview(showBackground = true, name = "Style Tab Row")
@Composable
fun PreviewStyleTabRow() {
    StyleTabRow(
        tabs = listOf("Trending", "Fashion", "Anime", "Digital Art", "Painting"),
        selectedTab = "Trending",
        onTabSelected = {}
    )
}

@Preview(showBackground = true, name = "Style Card - Not Selected")
@Composable
fun PreviewStyleCardNotSelected() {
    StyleCard(style = StyleItem(R.drawable.ic_gallery, "Novelistic"), selected = false)
}

@Preview(showBackground = true, name = "Style Card - Selected")
@Composable
fun PreviewStyleCardSelected() {
    StyleCard(style = StyleItem(R.drawable.ic_gallery, "Novelistic"), selected = true)
}

@Preview(showBackground = true, name = "Style Grid")
@Composable
fun PreviewStyleGrid() {
    StyleGrid(styles = sampleStyles, modifier = Modifier)
}

