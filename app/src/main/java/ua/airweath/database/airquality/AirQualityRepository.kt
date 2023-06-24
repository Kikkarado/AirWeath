package ua.airweath.database.airquality

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AirQualityRepository @Inject constructor(
    private val airQualityDao: AirQualityDao,
) {
    suspend fun upsertAirQuality(airQuality: AirQuality) {
        airQualityDao.upsertAirQuality(airQuality = airQuality)
    }

    suspend fun upsertAirQualities(airQualities: List<AirQuality>) {
        airQualityDao.upsertAirQualities(airQualities = airQualities)
    }

    fun getAirQualities(): Flow<List<AirQuality>> {
        return airQualityDao.getAirQualities()
    }

    fun getAirQualitiesByPlaceUUID(placeUUID: String): Flow<List<AirQuality>> {
        return airQualityDao.getAirQuality(placeUUID = placeUUID)
    }

    fun getAirQualitiesById(id: String): Flow<List<AirQuality>> {
        return airQualityDao.getAirQualitiesById(id = id)
    }

    fun getAirQualityById(id: String): Flow<AirQuality?> {
        return airQualityDao.getAirQualityById(id = id)
    }

    suspend fun deleteAirQuality(airQuality: AirQuality) {
        airQualityDao.deleteAirQuality(airQuality = airQuality)
    }

    suspend fun deleteAirQuality(placeUUID: String, time: String) {
        airQualityDao.deleteAirQuality(placeUUID = placeUUID, time = time)
    }

    suspend fun deleteAirQualityByTime(time: String) {
        airQualityDao.deleteAirQualityByTime(time = time)
    }

    suspend fun deleteAllAirQuality() {
        airQualityDao.deleteAllAirQuality()
    }
}