package com.example.aiartexample.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.example.aiartexample.model.AiArtCategory
import com.example.aiartexample.model.AiArtStyle
import com.example.core.R
import com.example.core.designsystem.component.AperoTextView
import com.example.core.designsystem.style.LocalCustomColors
import com.example.core.designsystem.style.LocalCustomTypography
import com.example.core.designsystem.style.pxToDp

@Composable
fun ChooseStyleScreen(
    modifier: Modifier = Modifier,
    categories: List<AiArtCategory>,
    selectedCategoryIndex: Int = 0,
    selectedStyleIndex: Int = 0,
    styles: List<AiArtStyle>,
    onStyleClick: (AiArtStyle) -> Unit = {},
    onCategoryClick: (AiArtCategory) -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(
            text = "Choose your Style",
            style = LocalCustomTypography.current.Title3.medium,
            modifier = Modifier.padding(bottom = 8.pxToDp()),
            color = LocalCustomColors.current.material.primary
        )

        Spacer(modifier = Modifier.height(4.pxToDp()))

        StyleTabRow(
            categories = categories,
            selectedCategoryIndex = selectedCategoryIndex,
            onCategoryClick = onCategoryClick
        )

        Spacer(modifier = Modifier.height(16.pxToDp()))

        StyleGrid(
            styles = styles,
            modifier = Modifier.fillMaxWidth(),
            onStyleClick = { style ->
                onStyleClick(style)
            },
            selectedStyleIndex = selectedStyleIndex
        )
    }
}

@Composable
fun StyleTabRow(
    categories: List<AiArtCategory>,
    selectedCategoryIndex: Int = 0,
    onCategoryClick: (AiArtCategory) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = selectedCategoryIndex,
        edgePadding = 0.pxToDp(),
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[selectedCategoryIndex]),
                color = LocalCustomColors.current.material.primary
            )
        }
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = selectedCategoryIndex == index,
                onClick = { onCategoryClick(category) },
                selectedContentColor = LocalCustomColors.current.material.primary,
                unselectedContentColor = Color.Black
            ) {
                AperoTextView(
                    text = category.name,
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
fun StyleCard(
    modifier: Modifier = Modifier,
    style: AiArtStyle,
    isSelected: Boolean = false,
    onStyleClick: (AiArtStyle) -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(80.pxToDp())
    ) {
        Box(
            modifier = Modifier
                .size(80.pxToDp())
                .clip(RoundedCornerShape(12.pxToDp()))
                .background(Color.Gray)
                .border(
                    width = if (isSelected) 2.pxToDp() else 0.pxToDp(),
                    color = if (isSelected) Color.Cyan else Color.Transparent,
                    shape = RoundedCornerShape(12.pxToDp())
                )
                .clickable { onStyleClick(style) }
        ) {
            AsyncImage(
                model = style.thumbnail,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )
            }
        }

        Spacer(modifier = Modifier.height(4.pxToDp()))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 88.pxToDp())
                .fillMaxWidth()
        ) {
            AperoTextView(
                text = style.name,
                textStyle = LocalCustomTypography.current.Caption1.regular,
                modifier = Modifier.padding(top = 4.pxToDp()),
                maxLines = 1,
                marqueeEnabled = true
            )
        }
    }
}


@Composable
fun StyleGrid(
    styles: List<AiArtStyle>,
    selectedStyleIndex: Int,
    modifier: Modifier = Modifier,
    onStyleClick: (AiArtStyle) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.pxToDp())
    ) {
        styles.forEach { style ->
            StyleCard(
                style = style ,
                onStyleClick = {
                    onStyleClick(it)
                },
                isSelected = selectedStyleIndex == styles.indexOf(style)
            )
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
    /*ChooseStyleScreen(
        modifier = Modifier,
        categories = listOf("Trending", "Fashion", "Anime", "Digital Art", "Painting"),
        selectedCategoryIndex = 2,
        onTabSelected = {},
        styles = sampleStyles
    )*/
}

