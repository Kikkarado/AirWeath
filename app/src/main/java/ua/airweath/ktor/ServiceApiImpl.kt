package ua.airweath.ktor

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlinx.coroutines.flow.first
import ua.airweath.BuildConfig
import ua.airweath.datastore.ProtoApi
import ua.airweath.ktor.data.currentaqi.AQIResponse
import ua.airweath.ktor.data.currentweather.WeatherResponse
import ua.airweath.ktor.data.geocoding.GeocodingData
import ua.airweath.ktor.data.ipinfo.IPInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServiceApiImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val client: HttpClient,
    private val protoApi: ProtoApi,
) : ServiceApi, SafeApiCall() {

    override suspend fun getIP(): NetworkResponse<String> {
        return safeCall(context) {
            client.get(HttpRoutes.GET_IP)
        }
    }

    override suspend fun getLocationByIP(): NetworkResponse<IPInfo> {
        val ipResponse = getIP()
        val ip = if (ipResponse is NetworkResponse.Success)
            ipResponse.data
        else ""

        val response: NetworkResponse<IPInfo> = safeCall(context) {
            client.get(HttpRoutes.getLocationByIPFromIPINFO(ip)) {
                contentType(ContentType.Application.Json)
            }
        }

        return if (response is NetworkResponse.Success) {
            response
        } else {
            safeCall(context) {
                client.get(HttpRoutes.getLocationByIPFromIPAPI(ip)) {
                    contentType(ContentType.Application.Json)
                }
            }
        }
    }

    suspend fun getLatLngByIp(): NetworkResponse<String> {
        return safeCall(context) {
            client.get(HttpRoutes.GET_LAT_LNG)
        }
    }

    override suspend fun getGeocodingByLatLngDefault(): NetworkResponse<GeocodingData> {
        val latLngResponse = getLocationByIP()
        val latLng = if (latLngResponse is NetworkResponse.Success) {
            if (latLngResponse.data.latitude != null && latLngResponse.data.longitude != null)
                "${latLngResponse.data.latitude}, ${latLngResponse.data.longitude}"
            else latLngResponse.data.loc ?: ""
        } else {
            val lanLngIp = getLatLngByIp()
            if (lanLngIp is NetworkResponse.Success) {
                lanLngIp.data
            } else ""
        }
        val lang = protoApi.locale.first()
        return safeCall(context) {
            client.get(HttpRoutes.GEOCODING_URL) {
                contentType(ContentType.Application.Json)
                parameters {
                    parameter("latlng", latLng)
                    parameter("sensor", false)
                    parameter("language", lang)
                    parameter("key", BuildConfig.geocodingKey)
                }
            }
        }
    }

    private suspend fun getAqi(
        latLng: LatLng,
        pastDays: Int? = null,
        _startDate: LocalDateTime = LocalDateTime.now(),
        _lastDate: LocalDateTime = _startDate.plusDays(4),
    ): NetworkResponse<AQIResponse> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = formatter.format(_startDate)
        val lastDate = formatter.format(_lastDate)
        return safeCall(context) {
            client.get(HttpRoutes.GET_CURRENT_AQI) {
                contentType(ContentType.Application.Json)
                parameters {
                    parameter(UrlParams.LATITUDE, latLng.latitude)
                    parameter(UrlParams.LONGITUDE, latLng.longitude)
                    parameter(
                        UrlParams.HOURLY,
                        HourlyParamsAQI.values().joinToString(separator = ",") { it.param }
                    )
                    parameter(
                        UrlParams.TIMEZONE,
                        if (TimeZone.getDefault().id == "Europe/Kyiv") "Europe/Kiev" else TimeZone.getDefault().id
                    )
                    if (pastDays != null) {
                        parameter(UrlParams.PAST_DAYS, pastDays)
                    } else {
                        parameter(UrlParams.START_DATE, startDate)
                        parameter(UrlParams.END_DATE, lastDate)
                    }
                }
            }
        }
    }

    override suspend fun getCurrentAqi(latLng: LatLng): NetworkResponse<AQIResponse> {
        val date = LocalDateTime.now()
        return getAqi(latLng = latLng, _startDate = date)
    }

    override suspend fun getPastDaysAqi(
        latLng: LatLng,
        pastDays: Int
    ): NetworkResponse<AQIResponse> {
        return getAqi(latLng = latLng, pastDays = pastDays)
    }

    override suspend fun getCurrentWeather(latLng: LatLng): NetworkResponse<WeatherResponse> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDateTime.now()
        val currentDate = formatter.format(date)
        val lastDate = formatter.format(date.plusDays(4))
        return safeCall(context) {
            client.get(HttpRoutes.GET_CURRENT_WEATHER) {
                contentType(ContentType.Application.Json)
                parameters {
                    parameter(UrlParams.LATITUDE, latLng.latitude)
                    parameter(UrlParams.LONGITUDE, latLng.longitude)
                    parameter(
                        UrlParams.HOURLY,
                        HourlyParamsWeather.values().joinToString(separator = ",") { it.param }
                    )
                    parameter(
                        UrlParams.TIMEZONE,
                        if (TimeZone.getDefault().id == "Europe/Kyiv") "Europe/Kiev" else TimeZone.getDefault().id
                    )
                    parameter(UrlParams.START_DATE, currentDate)
                    parameter(UrlParams.END_DATE, lastDate)
                }
            }
        }
    }

}