package ua.airweath.ktor.data.currentaqi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourlyUnits(
    @SerialName("time"               ) val time            : String? = null,
    @SerialName("pm10"               ) val pm10            : String? = null,
    @SerialName("pm2_5"              ) val pm25            : String? = null,
    @SerialName("carbon_monoxide"    ) val carbonMonoxide  : String? = null,
    @SerialName("nitrogen_dioxide"   ) val nitrogenDioxide : String? = null,
    @SerialName("sulphur_dioxide"    ) val sulphurDioxide  : String? = null,
    @SerialName("ozone"              ) val ozone           : String? = null,
    @SerialName("dust"               ) val dust            : String? = null,
    @SerialName("ammonia"            ) val ammonia         : String? = null,
    @SerialName("european_aqi"       ) val europeanAqi     : String? = null,
    @SerialName("european_aqi_pm2_5" ) val europeanAqiPm25 : String? = null,
    @SerialName("european_aqi_pm10"  ) val europeanAqiPm10 : String? = null,
    @SerialName("european_aqi_no2"   ) val europeanAqiNo2  : String? = null,
    @SerialName("european_aqi_o3"    ) val europeanAqiO3   : String? = null,
    @SerialName("european_aqi_so2"   ) val europeanAqiSo2  : String? = null,
    @SerialName("us_aqi"             ) val usAqi           : String? = null,
    @SerialName("us_aqi_pm2_5"       ) val usAqiPm25       : String? = null,
    @SerialName("us_aqi_pm10"        ) val usAqiPm10       : String? = null,
    @SerialName("us_aqi_no2"         ) val usAqiNo2        : String? = null,
    @SerialName("us_aqi_co"          ) val usAqiCo         : String? = null,
    @SerialName("us_aqi_o3"          ) val usAqiO3         : String? = null,
    @SerialName("us_aqi_so2"         ) val usAqiSo2        : String? = null,
)
