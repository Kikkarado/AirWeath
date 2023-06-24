package ua.airweath.ktor.data.geocoding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bounds(
    @SerialName("northeast")
    val northeast: Northeast? = null,
    @SerialName("southwest")
    val southwest: Southwest? = null,
)