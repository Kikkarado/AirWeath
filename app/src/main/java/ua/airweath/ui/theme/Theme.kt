package ua.airweath.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors()

private val LightColorPalette = lightColors(
    primary = Azure,
    primaryVariant = Dodger,
    /*secondary = StarDust,
    secondaryVariant = ,*/
    background = White,
    surface = White,
    error = ValentineRed,
    onPrimary = White,
    //onSecondary = ,
    onBackground = CharcoalGrey,
    onSurface = CharcoalGrey,
    onError = Seashell,
)

@Composable
fun AirWeathTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    val systemUi = rememberSystemUiController()

    systemUi.setStatusBarColor(
        color = White,
        darkIcons = true
    )

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}