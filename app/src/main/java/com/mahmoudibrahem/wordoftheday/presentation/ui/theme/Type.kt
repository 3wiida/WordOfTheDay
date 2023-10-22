package com.mahmoudibrahem.wordoftheday.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mahmoudibrahem.wordoftheday.R

// Set of Material typography styles to start with

val appFont = FontFamily(
    fonts = listOf(
        Font(R.font.opensans_bold, weight = FontWeight.Bold),
        Font(R.font.opensans_medium, weight = FontWeight.Medium),
        Font(R.font.opensans_regular, weight = FontWeight.Normal),
        Font(R.font.opensans_light, weight = FontWeight.Light),
        Font(R.font.opensans_semibold, weight = FontWeight.SemiBold),
        Font(R.font.opensans_extrabold, weight = FontWeight.ExtraBold),
        Font(R.font.opensans_regualr_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
        Font(R.font.opensans_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic)
    )
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = appFont,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = appFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = appFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = appFont,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = appFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
