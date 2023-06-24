package ua.airweath.utils

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import ua.airweath.R
import ua.airweath.enums.EUAqiRanges
import ua.airweath.enums.USAqiRanges
import ua.airweath.ui.theme.Azure
import ua.airweath.ui.theme.EUAQIExtremelyPoor
import ua.airweath.ui.theme.EUAQIFair
import ua.airweath.ui.theme.EUAQIGood
import ua.airweath.ui.theme.EUAQIModerate
import ua.airweath.ui.theme.EUAQIPoor
import ua.airweath.ui.theme.EUAQIVeryPoor
import ua.airweath.ui.theme.USSQIGood
import ua.airweath.ui.theme.USSQIHazardous
import ua.airweath.ui.theme.USSQIModerate
import ua.airweath.ui.theme.USSQIUnhealthy
import ua.airweath.ui.theme.USSQIUnhealthyForSensitiveGroups
import ua.airweath.ui.theme.USSQIVeryUnhealthy

object AQIBrush {
    fun getUSAQIBrush(): Brush {
        return Brush.sweepGradient(
            0.125f to USSQIGood,
            0.250f to USSQIModerate,
            0.375f to USSQIUnhealthyForSensitiveGroups,
            0.500f to USSQIUnhealthy,
            0.625f to USSQIVeryUnhealthy,
            0.875f to USSQIHazardous
        )
    }

    fun getEUAQIBrush(): Brush {
        return Brush.sweepGradient(
            0.125f to EUAQIGood,
            0.275f to EUAQIFair,
            0.425f to EUAQIModerate,
            0.575f to EUAQIPoor,
            0.725f to EUAQIVeryPoor,
            0.875f to EUAQIExtremelyPoor
        )
    }
}

object AqiPercent {
    fun usPercent(aqi: Int?): Int {
        if (aqi == null) return 0
        return aqi * 100 / 500
    }

    fun euPercent(aqi: Int?): Int {
        if (aqi == null) return 0
        return aqi * 100 / 100
    }
}

object AqiColor {
    fun usAqiColor(aqi: Int?): Color {
        return when (aqi) {
            in USAqiRanges.Good.range -> USSQIGood
            in USAqiRanges.Moderate.range -> USSQIModerate
            in USAqiRanges.UnhealthyForSensitiveGroups.range -> USSQIUnhealthyForSensitiveGroups
            in USAqiRanges.Unhealthy.range -> USSQIUnhealthy
            in USAqiRanges.VeryUnhealthy.range -> USSQIVeryUnhealthy
            in USAqiRanges.Hazardous.range -> USSQIHazardous
            else -> Color.Unspecified
        }
    }

    fun euAqiColor(aqi: Int?): Color {
        return when (aqi) {
            in EUAqiRanges.Good.range -> EUAQIGood
            in EUAqiRanges.Fair.range -> EUAQIFair
            in EUAqiRanges.Moderate.range -> EUAQIModerate
            in EUAqiRanges.Poor.range -> EUAQIPoor
            in EUAqiRanges.VeryPoor.range -> EUAQIVeryPoor
            in EUAqiRanges.ExtremelyPoor.range -> EUAQIExtremelyPoor
            else -> Color.Unspecified
        }
    }
}

object AQIRecommendation {
    fun usAqi(aqi: Int?): Int {
        return when (aqi) {
            in USAqiRanges.Good.range -> R.string.aqi_good
            in USAqiRanges.Moderate.range -> R.string.aqi_moderate
            in USAqiRanges.UnhealthyForSensitiveGroups.range -> R.string.aqi_unhealthy_for_sensitive_groups
            in USAqiRanges.Unhealthy.range -> R.string.aqi_unhealthy
            in USAqiRanges.VeryUnhealthy.range -> R.string.aqi_very_unhealthy
            in USAqiRanges.Hazardous.range -> R.string.aqi_hazardous
            else -> R.string.empty
        }
    }
    fun euAqi(aqi: Int?): Int {
        return when (aqi) {
            in EUAqiRanges.Good.range -> R.string.aqi_good
            in EUAqiRanges.Fair.range -> R.string.aqi_moderate
            in EUAqiRanges.Moderate.range -> R.string.aqi_unhealthy_for_sensitive_groups
            in EUAqiRanges.Poor.range -> R.string.aqi_unhealthy
            in EUAqiRanges.VeryPoor.range -> R.string.aqi_very_unhealthy
            in EUAqiRanges.ExtremelyPoor.range -> R.string.aqi_hazardous
            else -> R.string.empty
        }
    }
}

object AQIShortDescription {
    fun usAqi(aqi: Int?): Int {
        return when (aqi) {
            in USAqiRanges.Good.range -> R.string.aqi_us_good
            in USAqiRanges.Moderate.range -> R.string.aqi_us_moderate
            in USAqiRanges.UnhealthyForSensitiveGroups.range -> R.string.aqi_us_unhealthy_for_sensitive_groups
            in USAqiRanges.Unhealthy.range -> R.string.aqi_us_unhealthy
            in USAqiRanges.VeryUnhealthy.range -> R.string.aqi_us_very_unhealthy
            in USAqiRanges.Hazardous.range -> R.string.aqi_us_hazardous
            else -> R.string.empty
        }
    }
    fun euAqi(aqi: Int?): Int {
        return when (aqi) {
            in EUAqiRanges.Good.range -> R.string.aqi_eu_good
            in EUAqiRanges.Fair.range -> R.string.aqi_eu_fair
            in EUAqiRanges.Moderate.range -> R.string.aqi_eu_moderate
            in EUAqiRanges.Poor.range -> R.string.aqi_eu_poor
            in EUAqiRanges.VeryPoor.range -> R.string.aqi_eu_very_poor
            in EUAqiRanges.ExtremelyPoor.range -> R.string.aqi_eu_extremely_poor
            else -> R.string.empty
        }
    }
}