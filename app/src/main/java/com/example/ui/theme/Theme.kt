package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BrandBlue,
    onPrimary = Color.White,
    primaryContainer = BrandBlueLight,
    onPrimaryContainer = BrandBlueDark,
    secondary = EmeraldGreen,
    onSecondary = Color.White,
    tertiary = ProGold,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderLight
)

private val LightColorScheme = lightColorScheme(
    primary = BrandBlueDark,
    onPrimary = Color.White,
    primaryContainer = BrandBlueLight,
    onPrimaryContainer = BrandBlueDark,
    secondary = EmeraldGreen,
    onSecondary = Color.White,
    secondaryContainer = EmeraldGreenLight,
    onSecondaryContainer = Color(0xFF065F46),
    tertiary = ProGold,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderLight
)

fun parseHexColor(hex: String, fallback: Color): Color {
    return try {
        val clean = if (hex.startsWith("#")) hex else "#$hex"
        Color(android.graphics.Color.parseColor(clean))
    } catch (e: Exception) {
        fallback
    }
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    customPrimaryHex: String = "#0284C7",
    content: @Composable () -> Unit,
) {
    val primaryColor = parseHexColor(customPrimaryHex, BrandBlue)
    val primaryContainer = primaryColor.copy(alpha = 0.14f)

    val lightScheme = LightColorScheme.copy(
        primary = primaryColor,
        primaryContainer = primaryContainer,
        onPrimaryContainer = primaryColor
    )

    val darkScheme = DarkColorScheme.copy(
        primary = primaryColor,
        primaryContainer = primaryContainer,
        onPrimaryContainer = primaryColor
    )

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
