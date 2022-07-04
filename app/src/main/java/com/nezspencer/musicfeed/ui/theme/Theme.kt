package com.nezspencer.musicfeed.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Black_1D1D1D_Alpha94,
    primaryVariant = Black_1D1D1D_Alpha94,
    secondary = Black_1D1D1D_Alpha94,
    background = Black_1D1D1D_Alpha94,
    onSurface = Color.White,
    surface = Gray_767680
)

@Composable
fun MusicFeedTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}