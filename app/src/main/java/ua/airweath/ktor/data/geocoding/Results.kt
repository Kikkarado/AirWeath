package ua.airweath.ktor.data.geocoding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Results(
    @SerialName("address_components")
    val addressComponents: List<AddressComponents>? = null,
    @SerialName("formatted_address")
    val formattedAddress: String? = null,
    @SerialName("geometry")
    val geometry: Geometry? = null,
    @SerialName("place_id")
    val placeId: String? = null,
    @SerialName("plus_code")
    val plusCode: PlusCode? = null,
    @SerialName("types")
    val types: List<String>? = null,
)