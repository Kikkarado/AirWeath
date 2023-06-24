package ua.airweath.usecase.update

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import ua.airweath.data.mappers.toAirQualityList
import ua.airweath.data.mappers.toWeatherList
import ua.airweath.database.airquality.AirQuality
import ua.airweath.database.airquality.AirQualityRepository
import ua.airweath.database.weather.Weather
import ua.airweath.database.weather.WeatherRepository
import ua.airweath.ktor.NetworkResponse
import ua.airweath.ktor.ServiceApi
import javax.inject.Inject
import javax.inject.Singleton

interface IUpdateUseCase {
    suspend fun updateAll(latLng: LatLng, uuid: String): Pair<List<AirQuality>?, List<Weather>?>
    suspend fun updateAqi(latLng: LatLng, uuid: String): List<AirQuality>?
    suspend fun updateWeather(latLng: LatLng, uuid: String): List<Weather>?
}

@Singleton
class UpdateUseCase @Inject constructor(
    private val serviceApi: ServiceApi,
    private val airQualityRepository: AirQualityRepository,
    private val weatherRepository: WeatherRepository,
) : IUpdateUseCase {

    override suspend fun updateAll(latLng: LatLng, uuid: String): Pair<List<AirQuality>?, List<Weather>?> {
        return coroutineScope {
            val updateAqi = async {
                updateAqi(latLng, uuid).also {
                    if (!it.isNullOrEmpty()) Timber.d("Get AQI success")
                    else Timber.d("Get AQI error")
                }
            }
            val updateWeather = async {
                updateWeather(latLng, uuid).also {
                    if (!it.isNullOrEmpty()) Timber.d("Get weather success")
                    else Timber.d("Get weather error")
                }
            }
            Pair(updateAqi.await(), updateWeather.await())
        }
    }

    override suspend fun updateAqi(latLng: LatLng, uuid: String): List<AirQuality>? {
        serviceApi.getCurrentAqi(latLng).also { response ->
            if (response is NetworkResponse.Success) {
                val airQualities = response.data.hourly?.toAirQualityList(uuid) ?: return null
                if (airQualityRepository.getAirQualitiesByPlaceUUID(uuid).firstOrNull() == null) {
                    airQualityRepository.upsertAirQualities(airQualities)
                } else {
                    airQualities.forEach { airQuality ->
                        airQualityRepository.getAirQualityById(
                            "${airQuality.time} $uuid"
                        ).firstOrNull().also {
                            if (it == null) {
                                airQualityRepository.upsertAirQuality(airQuality)
                                Timber.d("AirQuality added ${airQuality.id}")
                            } else {
                                airQualityRepository.upsertAirQuality(airQuality.copy(id = it.id))
                                Timber.d("AirQuality updated ${it.id}")
                            }
                        }
                    }
                }
                return airQualities
            }
        }
        return null
    }

    override suspend fun updateWeather(latLng: LatLng, uuid: String): List<Weather>? {
        serviceApi.getCurrentWeather(latLng).also { response ->
            if (response is NetworkResponse.Success) {
                val weathers = response.data.hourly?.toWeatherList(uuid) ?: return null
                if (weatherRepository.getWeather(uuid).firstOrNull() == null) {
                    weatherRepository.upsertWeathers(weathers)
                } else {
                    weathers.forEach { weather ->
                        weatherRepository.getWeatherById("${weather.time} $uuid").firstOrNull().also {
                            if (it == null) {
                                weatherRepository.upsertWeather(weather)
                                Timber.d("Weather added ${weather.id}")
                            } else {
                                weatherRepository.upsertWeather(weather.copy(id = it.id))
                                Timber.d("Weather updated ${it.id}")
                            }
                        }
                    }
                }
                return weathers
            }
        }
        return null
    }
}