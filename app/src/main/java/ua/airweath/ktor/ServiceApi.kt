package ua.airweath.ktor

import com.google.android.gms.maps.model.LatLng
import ua.airweath.ktor.data.currentaqi.AQIResponse
import ua.airweath.ktor.data.currentweather.WeatherResponse
import ua.airweath.ktor.data.geocoding.GeocodingData
import ua.airweath.ktor.data.ipinfo.IPInfo

interface ServiceApi {

    suspend fun getIP(): NetworkResponse<String>

    suspend fun getLocationByIP(): NetworkResponse<IPInfo>

    suspend fun getGeocodingByLatLngDefault(): NetworkResponse<GeocodingData>

    suspend fun getCurrentAqi(latLng: LatLng): NetworkResponse<AQIResponse>

    suspend fun getCurrentWeather(latLng: LatLng): NetworkResponse<WeatherResponse>

    suspend fun getPastDaysAqi(latLng: LatLng, pastDays: Int): NetworkResponse<AQIResponse>

}