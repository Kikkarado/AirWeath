package ua.airweath.ktor

enum class HourlyParamsAQI(val param: String) {

    PM10("pm10"),

    PM2_5("pm2_5"),

    CARBON_MONOXIDE("carbon_monoxide"),

    NITROGEN_DIOXIDE("nitrogen_dioxide"),

    SULPHUR_DIOXIDE("sulphur_dioxide"),

    OZONE("ozone"),

    DUST("dust"),

    AMMONIA("ammonia"),

    EUROPEAN_AQI("european_aqi"),
    EUROPEAN_AQI_PM2_5("european_aqi_pm2_5"),
    EUROPEAN_AQI_PM10("european_aqi_pm10"),
    EUROPEAN_AQI_NO2("european_aqi_no2"),
    EUROPEAN_AQI_O3("european_aqi_o3"),
    EUROPEAN_AQI_SO2("european_aqi_so2"),

    US_AQI("us_aqi"),
    US_AQI_PM2_5("us_aqi_pm2_5"),
    US_AQI_PM10("us_aqi_pm10"),
    US_AQI_NO2("us_aqi_no2"),
    US_AQI_O3("us_aqi_o3"),
    US_AQI_SO2("us_aqi_so2"),
    US_AQI_CO("us_aqi_co"),

}

enum class HourlyParamsWeather(val param: String) {
    TEMPERATURE_2M("temperature_2m"),
    RELATIVEHUMIDITY_2M("relativehumidity_2m"),
    APPARENT_TEMPERATURE("apparent_temperature"),
    PRECIPITATION("precipitation"),
    RAIN("rain"),
    WEATHERCODE("weathercode"),
    WINDSPEED_10M("windspeed_10m"),
    WINDGUSTS_10M("windgusts_10m"),
}



object TimeFormatParams {

    const val UNIXTIME = "unixtime"

}