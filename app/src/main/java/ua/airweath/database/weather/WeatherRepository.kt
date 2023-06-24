package ua.airweath.database.weather

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherDao: WeatherDao,
) {

    suspend fun upsertWeather(weather: Weather) {
        weatherDao.upsertWeather(weather = weather)
    }

    suspend fun upsertWeathers(weathers: List<Weather>) {
        weatherDao.upsertWeathers(weathers = weathers)
    }

    fun getWeathers(): Flow<List<Weather>> {
        return weatherDao.getWeathers()
    }

    fun getWeather(placeUUID: String): Flow<List<Weather>> {
        return weatherDao.getWeather(placeUUID = placeUUID)
    }

    fun getWeathersById(id: String): Flow<List<Weather>> {
        return weatherDao.getWeathersById(id = id)
    }

    fun getWeatherById(id: String): Flow<Weather?> {
        return weatherDao.getWeatherById(id = id)
    }

    suspend fun deleteWeather(weather: Weather) {
        weatherDao.deleteWeather(weather = weather)
    }

    suspend fun deleteWeather(placeUUID: String, time: String) {
        weatherDao.deleteWeather(placeUUID = placeUUID, time = time)
    }

    suspend fun deleteWeatherByTime(time: String) {
        weatherDao.deleteWeatherByTime(time = time)
    }

    suspend fun deleteAllWeathers() {
        weatherDao.deleteAllWeathers()
    }
}