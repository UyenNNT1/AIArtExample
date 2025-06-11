package com.example.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.designsystem.style.LocalCustomColors
import com.example.core.designsystem.style.LocalCustomTypography
import com.example.core.designsystem.style.pxToDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    header: @Composable () -> Unit,
    content: String,
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false
            ),
            containerColor = LocalCustomColors.current.material.surface,
            contentColor = LocalCustomColors.current.material.onSurface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = LocalConfiguration.current.screenHeightDp.pxToDp() * 2/3)
                    .background(LocalCustomColors.current.material.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.pxToDp())
                        .verticalScroll(rememberScrollState())
                        .background(LocalCustomColors.current.material.surface)
                ) {
                    // Header
                    header()

                    // Content
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.pxToDp()),
                        shape = RoundedCornerShape(12.pxToDp()),
                        border = BorderStroke(
                            width = 1.pxToDp(),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                        ),
                        color = LocalCustomColors.current.material.surface
                    ) {
                        Text(
                            text = content,
                            style = LocalCustomTypography.current.Footnote.regular,
                            modifier = Modifier.padding(16.pxToDp()),
                            color = LocalCustomColors.current.material.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(16.pxToDp()))
                }
            }
        }
    }
}

// Example usage:
@Composable
fun BottomSheetDemo() {
    var showBottomSheet by remember { mutableStateOf(false) }

    Button(
        onClick = { showBottomSheet = true },
        modifier = Modifier.padding(16.pxToDp())
    ) {
        Text("Show Bottom Sheet")
    }

    CustomBottomSheet(
        showBottomSheet = showBottomSheet,
        onDismiss = { showBottomSheet = false },
        header = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.pxToDp())
                    .background(LocalCustomColors.current.material.surface),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.pxToDp())
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Hardiness zone",
                        style = MaterialTheme.typography.titleMedium,
                        color = LocalCustomColors.current.material.onSurface
                    )
                }
            }
        },
        content = "Your content text goes here..."
    )
}

// Preview
@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    name = "Bottom Sheet Demo"
)
@Composable
fun BottomSheetDemoPreview() {
    MaterialTheme {
        BottomSheetDemo()
    }
}