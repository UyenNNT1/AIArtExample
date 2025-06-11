package com.example.core.designsystem.style

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

object AppColors {
    val Main = Color(0xFFE400D9)
    val Secondary = Color(0xFF1D00F5)
    val Black = Color(0xFF000000)
    val White = Color(0xFFFFFFFF)
    val Background = Color(0xFFF5F5F5)


    object Light {
        val Border = Color(0xFFEAEAEA)
        val TextColorPrimary = Color(0xFFFFFFFF)
        val TextColorHighMedium = Color(0xFFFFC457)
        val TextColorSecondary = Color(0xFF848890)

        object Text {
            val Disable = Grey.Grey400
            val Error = Color(0xFFF53F3F)
        }

        object Grey {
            val Grey50 = Color(0xFFF1F1F1)
            val Grey100 = Color(0xFFF7F7F7)
            val Grey300 = Color(0xFFEAEAEA)
            val Grey400 = Color(0xFFB0B1B2)
            val Grey600 = Color(0xFF797C80)
            val Grey700 = Color(0xFF1D1D21)
            val Grey900 = Color(0xFF131318)
        }

        object Line {
            val ProfileLine = Color(0xFF9E9E9E)
        }
    }

    object Dark {

        val Border = Color(0xFFEAEAEA)
        val TextColorPrimary = Color(0xFFFFFFFF)
        val TextColorHighMedium = Color(0xFFFFC457)
        val TextColorSecondary = Color(0xFF848890)

        object Text {
            val Disable = Grey.Grey400
            val Error = Color(0xFFF53F3F)
        }

        object Grey {
            val Grey900 = Color(0xFF141414)
            val Grey600 = Color(0xFF202020)
            val Grey400 = Color(0xFF303030)
            val Grey300 = Color(0xFF434343)
            val Grey200 = Color(0xFF2E2E2E)
            val Grey100 = Color(0xFF848890)
            val Grey50 = Color(0xFFEDEAEA)
        }

        object Line {
            val ProfileLine = Color(0xFF9E9E9E)
        }
    }
}

data class CustomColorScheme(
    val material: ColorScheme,
    val textPrimary: Color,
    val textHighMediumEmp: Color,
    val textSecondary: Color,
    val textDisable: Color,
    val textWhite: Color,
    val border: Color,
)