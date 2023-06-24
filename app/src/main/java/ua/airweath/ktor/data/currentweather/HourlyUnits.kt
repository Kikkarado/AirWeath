package ua.airweath.ktor.data.currentweather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyUnits(
    @SerialName("time")
    val time: String? = null,
    @SerialName("temperature_2m")
    val temperature2m: String? = null,
    @SerialName("relativehumidity_2m")
    val relativehumidity2m: String? = null,
    @SerialName("apparent_temperature")
    val apparentTemperature: String? = null,
    @SerialName("precipitation")
    val precipitation: String? = null,
    @SerialName("rain")
    val rain: String? = null,
    @SerialName("weathercode")
    val weathercode: String? = null,
    @SerialName("windspeed_10m")
    val windspeed10m: String? = null,
    @SerialName("windgusts_10m")
    val windgusts10m: String? = null,
)
