package ua.airweath.ktor.data.currentaqi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hourly(
    @SerialName("time"               ) val time            : List<String>?    = null,
    @SerialName("pm10"               ) val pm10            : List<Float?>? = null,
    @SerialName("pm2_5"              ) val pm25            : List<Float?>?    = null,
    @SerialName("carbon_monoxide"    ) val carbonMonoxide  : List<Float?>?    = null,
    @SerialName("nitrogen_dioxide"   ) val nitrogenDioxide : List<Float?>? = null,
    @SerialName("sulphur_dioxide"    ) val sulphurDioxide  : List<Float?>? = null,
    @SerialName("ozone"              ) val ozone           : List<Float?>?    = null,
    @SerialName("dust"               ) val dust            : List<Float?>?    = null,
    @SerialName("ammonia"            ) val ammonia         : List<Float?>?    = null,
    @SerialName("european_aqi"       ) val europeanAqi     : List<Int?>?    = null,
    @SerialName("european_aqi_pm2_5" ) val europeanAqiPm25 : List<Int?>?    = null,
    @SerialName("european_aqi_pm10"  ) val europeanAqiPm10 : List<Int?>?    = null,
    @SerialName("european_aqi_no2"   ) val europeanAqiNo2  : List<Int?>?    = null,
    @SerialName("european_aqi_o3"    ) val europeanAqiO3   : List<Int?>?    = null,
    @SerialName("european_aqi_so2"   ) val europeanAqiSo2  : List<Int?>?    = null,
    @SerialName("us_aqi"             ) val usAqi           : List<Int?>?    = null,
    @SerialName("us_aqi_pm2_5"       ) val usAqiPm25       : List<Int?>?    = null,
    @SerialName("us_aqi_pm10"        ) val usAqiPm10       : List<Int?>?    = null,
    @SerialName("us_aqi_no2"         ) val usAqiNo2        : List<Int?>?    = null,
    @SerialName("us_aqi_co"          ) val usAqiCo         : List<Int?>?    = null,
    @SerialName("us_aqi_o3"          ) val usAqiO3         : List<Int?>?    = null,
    @SerialName("us_aqi_so2"         ) val usAqiSo2        : List<Int?>?    = null,
)
