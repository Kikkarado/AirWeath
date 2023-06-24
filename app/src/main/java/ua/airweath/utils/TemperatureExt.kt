package ua.airweath.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import ua.airweath.ui.theme.BlackRose
import ua.airweath.ui.theme.MutedBlue
import ua.airweath.ui.theme.OrangeSalmon
import ua.airweath.ui.theme.Rhino

object TemperatureExt {
    fun getBrush(): Brush {
        return Brush.sweepGradient(
            0.125f to Rhino,
            0.375f to MutedBlue,
            0.625f to OrangeSalmon,
            0.875f to BlackRose,
        )
    }
    fun tmpPercent(tmp: Float?): Int {
        if (tmp == null) return 0
        val minTmp = 50
        return ((tmp+minTmp) / 100 * 100).toInt()
    }
    fun getTmpColor(tmp: Float?): Color {
        if (tmp == null) return Color.Unspecified
        return when(tmp) {
            in Float.MIN_VALUE..-25.0f -> Rhino
            in -25.1f..0.0f -> MutedBlue
            in -0.1f..25.0f -> OrangeSalmon
            in 25.1f..Float.MAX_VALUE -> BlackRose
            else -> Color.Unspecified
        }
    }
}