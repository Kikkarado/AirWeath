package ua.airweath.ktor.data.geocoding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    @SerialName("bounds")
    val bounds: Bounds? = null,
    @SerialName("location")
    val location: Location? = null,
    @SerialName("location_type")
    val locationType: String? = null,
    @SerialName("viewport")
    val viewport: Viewport? = null,
)