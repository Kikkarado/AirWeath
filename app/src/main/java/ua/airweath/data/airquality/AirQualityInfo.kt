package ua.airweath.data.airquality

import ua.airweath.database.airquality.AirQuality

data class AirQualityInfo(
    val indexCurrent: Int?,
    val currentAirQuality: AirQuality?,
    val airQualityMap: Map<Int, List<AirQuality>>,
)
