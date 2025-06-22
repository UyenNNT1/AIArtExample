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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
    selectedCategoryIndex: Int,
    selectedStyleIndex: Int,
    styles: List<AiArtStyle>,
    onStyleClick: (Int) -> Unit = {},
    onCategoryClick: (Int) -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(
            text = "Choose your Style",
            style = LocalCustomTypography.current.Title3.medium,
            modifier = Modifier.padding(bottom = 8.pxToDp()),
            color = LocalCustomColors.current.material.primary
        )

        Spacer(modifier = Modifier.height(4.pxToDp()))

        CategoryList(
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
fun CategoryList(
    modifier: Modifier = Modifier,
    categories: List<AiArtCategory>,
    selectedCategoryIndex: Int,
    onCategoryClick: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier
    ) {
        itemsIndexed(categories, key = {index, category -> category.id}) { index, category ->
            CategoryItem(
                modifier = Modifier.padding(end = 8.pxToDp()),
                category = category,
                isSelected = selectedCategoryIndex == index,
                onCategoryClick = { onCategoryClick(index) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: AiArtCategory,
    isSelected: Boolean = false,
    onCategoryClick: () -> Unit = {}
){
    val color = if (isSelected) LocalCustomColors.current.material.primary else LocalCustomColors.current.material.onSurfaceVariant
    Column(
        modifier = modifier.clickable { onCategoryClick() }
    ) {
        Text(
            text = category.name,
            style = LocalCustomTypography.current.Body.regular,
            modifier = Modifier.padding(vertical = 8.pxToDp()),
            color = color
        )
        Spacer(modifier = Modifier.width(8.pxToDp()))
        Box(
            modifier = Modifier
                .size(height = 1.pxToDp(), width = 10.pxToDp())
                .background(color),
            contentAlignment = Alignment.Center
        ){}
    }
}
@Composable
fun StyleCard(
    modifier: Modifier = Modifier,
    style: AiArtStyle,
    isSelected: Boolean = false,
    onStyleClick: () -> Unit = {}
) {
    Column (
        modifier = modifier
            .width(80.pxToDp())
            .wrapContentHeight()
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
                .clickable { onStyleClick() }
        ) {
            AsyncImage(
                model = style.thumbnail,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.image_test),
                error = painterResource(id = R.drawable.image_test)
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

        Text(
            text = style.name,
            style = LocalCustomTypography.current.Caption1.regular,
            modifier = Modifier.padding(top = 4.pxToDp()).fillMaxWidth(),
            color = if (isSelected) Color.Cyan else Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StyleGrid(
    styles: List<AiArtStyle>,
    selectedStyleIndex: Int,
    modifier: Modifier = Modifier,
    onStyleClick: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.pxToDp())
    ) {
        itemsIndexed(styles, key = { index, style -> style.id }) { index, style ->
            StyleCard(
                style = style,
                onStyleClick = {
                    onStyleClick(index)
                },
                isSelected = selectedStyleIndex == index
            )
        }

    }
}

@Preview(showBackground = true, name = "Choose Style Screen")
@Composable
fun PreviewChooseStyleScreen() {

}

@Preview(showBackground = true, name = "Style Card")
@Composable
private fun StyleCardPreview() {
    StyleCard(
        style = AiArtStyle("style_2_0", "Neon", "https://example.com/style_2_0.jpg"),
        onStyleClick = {},
        isSelected = false
    )
}