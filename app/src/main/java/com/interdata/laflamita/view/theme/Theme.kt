package com.interdata.laflamita.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme()
private val LightColorScheme = lightColorScheme(
    primary = Jaffa,
    secondary = Bush,
    tertiary = Tapestry,

    onPrimary = onJaffa,
    onSecondary = onBush,
    onTertiary = onTapestry,

    background = background,
    onBackground = onBackground,
    surface = Sunshade,
    onSurface = onSunshade,
)

@Composable
fun LaFlamitaTheme(
    // darkTheme: Boolean = isSystemInDarkTheme(),
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}