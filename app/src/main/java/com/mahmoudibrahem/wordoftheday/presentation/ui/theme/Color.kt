package com.mahmoudibrahem.wordoftheday.presentation.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


val lightColorPalette = lightColorScheme(
    primary = Color(0xFF1f6bff),
    secondary = Color(0xFFf64538),
    tertiary = Color(0xFF40a965),
    background = Color(0xFFf9fafb),
    surface = Color(0xFFffffff),
    onSurface = Color(0xFF000000),
    onSurfaceVariant = Color(0xFFd8d9da),
    outline = Color(0xFFa2bef2),
    onBackground = Color(0xFF000000),

)

val darkColorPalette = darkColorScheme(
    primary = Color(0xFFdedede),
    secondary = Color(0xFFf64538),
    tertiary = Color(0xFF40a965),
    background = Color(0xFF202022),
    surface = Color(0xFF151517),
    onSurface = Color(0xFFe3e3e3),
    onSurfaceVariant = Color(0xFF545557),
    outline = Color(0xFF474748),
    onBackground = Color(0xFFffffff)
)