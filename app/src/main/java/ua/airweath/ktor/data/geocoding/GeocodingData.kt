package ua.airweath.ktor.data.geocoding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodingData(
    @SerialName("plus_code")
    val plusCode: PlusCode? = null,
    @SerialName("results")
    val results: List<Results>? = null,
    @SerialName("status")
    val status: String? = null,
)
