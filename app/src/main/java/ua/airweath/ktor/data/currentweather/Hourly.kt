package ua.airweath.ktor.data.currentweather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hourly(
    @SerialName("time")
    val time: List<String>? = null,
    @SerialName("temperature_2m")
    val temperature2m: List<Float>? = null,
    @SerialName("relativehumidity_2m")
    val relativehumidity2m: List<Int>? = null,
    @SerialName("apparent_temperature")
    val apparentTemperature: List<Float>? = null,
    @SerialName("precipitation")
    val precipitation: List<Float>? = null,
    @SerialName("rain")
    val rain: List<Float>? = null,
    @SerialName("weathercode")
    val weathercode: List<Int>? = null,
    @SerialName("windspeed_10m")
    val windspeed10m: List<Float>? = null,
    @SerialName("windgusts_10m")
    val windgusts10m: List<Float>? = null,
)
