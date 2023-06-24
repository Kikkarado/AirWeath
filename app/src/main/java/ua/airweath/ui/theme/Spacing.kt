package ua.airweath.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    /**0dp*/
    val zero: Dp = 0.dp,
    /**5dp*/
    val extraSmall: Dp = 5.dp,
    /**10dp*/
    val small: Dp = 10.dp,
    /**15dp*/
    val medium: Dp = 15.dp,
    /**20dp*/
    val large: Dp = 20.dp,
    /**25dp*/
    val larger: Dp = 25.dp,
    /**30dp*/
    val extraLarge: Dp = 30.dp,
)

val LocalSpacing = compositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current