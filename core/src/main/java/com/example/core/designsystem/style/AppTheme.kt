package com.example.core.designsystem.style

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration

private val lightCustomColorScheme = CustomColorScheme(
    material = lightColorScheme(
        primary = AppColors.Main,
        surface = AppColors.White,
        background = AppColors.Background,
        secondary = AppColors.Secondary,
        error = AppColors.Light.Text.Error
    ),
    textPrimary = AppColors.Light.TextColorPrimary,
    textHighMediumEmp = AppColors.Light.TextColorHighMedium,
    textSecondary = AppColors.Light.TextColorSecondary,
    textDisable = AppColors.Light.Text.Disable,
    textWhite = AppColors.White,
    border = AppColors.Light.Border
)

private val darkCustomColorScheme = CustomColorScheme(
    material = darkColorScheme(
        primary = AppColors.Main,
        surface = AppColors.White,
        background = AppColors.Background,
        secondary = AppColors.Secondary,
        error = AppColors.Dark.Text.Error
    ),
    textPrimary = AppColors.Dark.TextColorPrimary,
    textHighMediumEmp = AppColors.Dark.TextColorHighMedium,
    textSecondary = AppColors.Dark.TextColorSecondary,
    textDisable = AppColors.Dark.Text.Disable,
    textWhite = AppColors.White,
    border = AppColors.Dark.Border,
)

val LocalCustomColors = staticCompositionLocalOf { lightCustomColorScheme }
var cachedDensityScale: Float = 1f

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val customColorScheme = if (darkTheme) darkCustomColorScheme else lightCustomColorScheme
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Calculate scale based on iPhone 14 Pro Max width (430dp)
    val screenScale = remember(screenWidthDp) {
        val baseWidth = 430f  // iPhone 14 Pro Max width
        (screenWidthDp / baseWidth).also {
            cachedDensityScale = it
        }
    }
    val typography = rememberCustomTypography(screenScale)


    CompositionLocalProvider(
        LocalCustomColors provides customColorScheme,
        LocalCustomTypography provides typography,
        LocalScreenScale provides screenScale
    ) {

        MaterialTheme(
            colorScheme = customColorScheme.material,
            content = content
        )
    }
}