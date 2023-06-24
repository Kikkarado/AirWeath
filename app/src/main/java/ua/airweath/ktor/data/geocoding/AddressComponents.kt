package ua.airweath.ktor.data.geocoding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressComponents(
    @SerialName("long_name")
    val longName: String? = null,
    @SerialName("short_name")
    val shortName: String? = null,
    @SerialName("types")
    val types: List<String>? = null,
)