package ua.airweath.ktor.data.currentweather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("latitude")
    val latitude: Float? = null,
    @SerialName("longitude")
    val longitude: Float? = null,
    @SerialName("generationtime_ms")
    val generationtimeMs: Float? = null,
    @SerialName("utc_offset_seconds")
    val utcOffsetSeconds: Int? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String? = null,
    @SerialName("elevation")
    val elevation: Float? = null,
    @SerialName("hourly_units")
    val hourlyUnits: HourlyUnits? = null,
    @SerialName("hourly")
    val hourly: Hourly? = null,
)
