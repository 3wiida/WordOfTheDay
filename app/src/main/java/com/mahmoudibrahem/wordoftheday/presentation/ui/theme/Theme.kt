package com.mahmoudibrahem.wordoftheday.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LightColorPalette = lightColorScheme(
    primary = Color(0xFF1f6bff),
    secondary = Color(0xFFf64538),
    tertiary = Color(0xFF40a965),
    background = Color(0xFFf9fafb),
    surface = Color(0xFFffffff),
    onSurface = Color(0xFF000000),
    onSurfaceVariant = Color(0xFFd8d9da),
    outline = Color(0xFFa2bef2),
    onBackground = Color(0xFF000000),
    outlineVariant = Color(0x14A2BEF2),
    onPrimary = Color(0xFFf8f9f9)
)

val DarkColorPalette = darkColorScheme(
    primary = Color(0xFFffffff),
    secondary = Color(0xFFf64538),
    tertiary = Color(0xFF40a965),
    background = Color(0xFF202022),
    surface = Color(0xFF151517),
    onSurface = Color(0xFFe3e3e3),
    onSurfaceVariant = Color(0xFF545557),
    outline = Color(0xFF474748),
    onBackground = Color(0xFFffffff),
    outlineVariant = Color(0xAA2BEF2),
    onPrimary = Color(0xFF151517)
)

@Composable
fun WordOfTheDayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme.switch(),
        typography = Typography,
        content = content
    )
}

@Composable
private fun animateColor(targetValue: Color) =
    animateColorAsState(
        targetValue = targetValue,
        animationSpec = tween(durationMillis = 1000),
        label = ""
    ).value

@Composable
fun ColorScheme.switch() = copy(
    primary = animateColor(primary),
    onPrimary = animateColor(onPrimary),
    secondary = animateColor(secondary),
    tertiary = animateColor(tertiary),
    background = animateColor(background),
    surface = animateColor(surface),
    onSurfaceVariant = animateColor(onSurfaceVariant),
    onSecondary = animateColor(onSecondary),
    onBackground = animateColor(onBackground),
    onSurface = animateColor(onSurface),
    onError = animateColor(onError),
    outline = animateColor(outline),
    outlineVariant = animateColor(outlineVariant)
)