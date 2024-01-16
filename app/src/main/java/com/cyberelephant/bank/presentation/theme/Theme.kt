package com.cyberelephant.bank.presentation.theme

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = textOnPrimary,
    error = error,
    onError = error,
    errorContainer = error,
    onErrorContainer = error,
    onPrimary = textOnPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    background = background,
    onBackground = textOnPrimary,
    surface = backgroundModal,
    onSurface = textOnPrimary,
    surfaceVariant = firefly,
    onSurfaceVariant = textOnPrimary,
    outline = md_theme_dark_outline,
    scrim = md_theme_dark_scrim,
)

@Composable
fun BankManagementTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primaryContainer.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}

val borderWidth = 1.dp

val xSmallMargin = 6.dp
val smallMargin = 8.dp
val normalMargin = 12.dp
val largeMargin = 16.dp
val xLargeMargin = 20.dp
val xxLargeMargin = 24.dp
val xxxLargeMargin = 40.dp

val horizontalMargin = normalMargin * 2
val verticalMargin = normalMargin * 2

val modalBottomSheet = 400.dp

val roundedCornerRadius = 12.dp

val cardBorder = BorderStroke(borderWidth, cardOutlined)
val cardMinHeight = 90.dp
