package ua.airweath.ktor.data.geocoding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlusCode(
    @SerialName("compound_code")
    val compoundCode: String? = null,
    @SerialName("global_code")
    val globalCode: String? = null,
)