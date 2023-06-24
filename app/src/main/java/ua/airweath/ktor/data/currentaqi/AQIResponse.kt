package ua.airweath.ktor.data.currentaqi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AQIResponse(
    @SerialName("latitude"              ) val latitude             : Float?       = null,
    @SerialName("longitude"             ) val longitude            : Float?       = null,
    @SerialName("generationtime_ms"     ) val generationtimeMs     : Float?       = null,
    @SerialName("utc_offset_seconds"    ) val utcOffsetSeconds     : Int?         = null,
    @SerialName("timezone"              ) val timezone             : String?      = null,
    @SerialName("timezone_abbreviation" ) val timezoneAbbreviation : String?      = null,
    @SerialName("hourly_units"          ) val hourlyUnits          : HourlyUnits? = null,
    @SerialName("hourly"                ) val hourly               : Hourly?      = null,
    @SerialName("error"                 ) val error                : Boolean?     = null,
    @SerialName("reason"                ) val reason               : String?      = null,

    )
