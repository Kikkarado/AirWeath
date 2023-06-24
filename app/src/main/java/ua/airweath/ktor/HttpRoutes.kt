package ua.airweath.ktor

object HttpRoutes {

    private const val BASE_URL_AQI = "https://air-quality-api.open-meteo.com/v1/air-quality"
    private const val BASE_URL_WEATHER = "https://api.open-meteo.com/v1/forecast"

    const val GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json"

    const val GET_IP = "https://api.ipify.org/?format=text"

    /* Use https://ipapi.co/$ipAddress/json or https://ipinfo.io/$ipAddress/json */
    fun getLocationByIPFromIPINFO(ipAddress: String) = "https://ipinfo.io/$ipAddress/json"

    fun getLocationByIPFromIPAPI(ipAddress: String) = "https://ipapi.co/$ipAddress/json"

    const val GET_LAT_LNG = "https://ipapi.co/latlong/"

    const val GET_CURRENT_AQI = BASE_URL_AQI

    const val GET_CURRENT_WEATHER = BASE_URL_WEATHER

}