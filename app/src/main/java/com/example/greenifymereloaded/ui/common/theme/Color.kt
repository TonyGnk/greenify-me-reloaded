package com.example.greenifymereloaded.ui.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getThemeColorVariants(size: Int): List<Color> {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary

    return when {
        size <= 0 -> emptyList()
        size == 1 -> listOf(primary)
        size == 2 -> listOf(primary, secondary)
        size == 3 -> listOf(primary, secondary, tertiary)
        else -> {
            // Generate a larger palette by mixing and varying theme colors
            val colors = mutableListOf<Color>()
            val step = 1.0 / (size - 1)
            for (i in 0 until size) {
                val ratio = i * step
                when (i % 3) {
                    0 -> colors.add(primary.copy(alpha = 1f - ratio.toFloat()))
                    1 -> colors.add(secondary.copy(alpha = 1f - ratio.toFloat()))
                    2 -> colors.add(tertiary.copy(alpha = 1f - ratio.toFloat()))
                }
            }
            colors
        }
    }
}

val primaryLight = Color(0xFF036B5C)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFA0F2DF)
val onPrimaryContainerLight = Color(0xFF00201B)
val secondaryLight = Color(0xFF4A635D)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFCDE8E0)
val onSecondaryContainerLight = Color(0xFF06201B)
val tertiaryLight = Color(0xFF436278)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFC9E6FF)
val onTertiaryContainerLight = Color(0xFF001E2F)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFF5FBF7)
val onBackgroundLight = Color(0xFF171D1B)
val surfaceLight = Color(0xFFF5FBF7)
val onSurfaceLight = Color(0xFF171D1B)
val surfaceVariantLight = Color(0xFFDAE5E1)
val onSurfaceVariantLight = Color(0xFF3F4946)
val outlineLight = Color(0xFF6F7976)
val outlineVariantLight = Color(0xFFBEC9C5)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2B3230)
val inverseOnSurfaceLight = Color(0xFFECF2EF)
val inversePrimaryLight = Color(0xFF84D5C4)
val surfaceDimLight = Color(0xFFD5DBD8)
val surfaceBrightLight = Color(0xFFF5FBF7)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFEFF5F2)
val surfaceContainerLight = Color(0xFFE9EFEC)
val surfaceContainerHighLight = Color(0xFFE3EAE6)
val surfaceContainerHighestLight = Color(0xFFDEE4E1)


val primaryDark = Color(0xFF84D5C4)
val onPrimaryDark = Color(0xFF00382F)
val primaryContainerDark = Color(0xFF005045)
val onPrimaryContainerDark = Color(0xFFA0F2DF)
val secondaryDark = Color(0xFFB1CCC4)
val onSecondaryDark = Color(0xFF1C352F)
val secondaryContainerDark = Color(0xFF334B46)
val onSecondaryContainerDark = Color(0xFFCDE8E0)
val tertiaryDark = Color(0xFFABCAE4)
val onTertiaryDark = Color(0xFF123348)
val tertiaryContainerDark = Color(0xFF2B4A5F)
val onTertiaryContainerDark = Color(0xFFC9E6FF)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF0E1513)
val onBackgroundDark = Color(0xFFDEE4E1)
val surfaceDark = Color(0xFF0E1513)
val onSurfaceDark = Color(0xFFDEE4E1)
val surfaceVariantDark = Color(0xFF3F4946)
val onSurfaceVariantDark = Color(0xFFBEC9C5)
val outlineDark = Color(0xFF89938F)
val outlineVariantDark = Color(0xFF3F4946)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFDEE4E1)
val inverseOnSurfaceDark = Color(0xFF2B3230)
val inversePrimaryDark = Color(0xFF036B5C)
val surfaceDimDark = Color(0xFF0E1513)
val surfaceBrightDark = Color(0xFF343B38)
val surfaceContainerLowestDark = Color(0xFF090F0E)
val surfaceContainerLowDark = Color(0xFF171D1B)
val surfaceContainerDark = Color(0xFF1B211F)
val surfaceContainerHighDark = Color(0xFF252B29)
val surfaceContainerHighestDark = Color(0xFF303634)