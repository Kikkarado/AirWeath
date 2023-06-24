package ua.airweath.database.place

import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import ua.airweath.database.relations.PlaceWithAQIAndWeather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRepository @Inject constructor(
    private val placeDao: PlaceDao
) {

    suspend fun upsertPlace(place: Place) {
        placeDao.upsertPlace(place = place)
        Timber.d("Place upserted ${place.uuid}")
    }

    suspend fun upsertPlaces(places: List<Place>) {
        placeDao.upsertPlaces(places = places)
        Timber.d("Places upserted ${places.size}")
    }

    fun getPlaces(): Flow<List<PlaceWithAQIAndWeather>> {
        return placeDao.getPlaces()
    }

    fun getPlace(uuid: String): Flow<PlaceWithAQIAndWeather?> {
        return placeDao.getPlace(uuid = uuid)
    }

    fun getFavoritePlace(): Flow<PlaceWithAQIAndWeather?> {
        return placeDao.getFavoritePlace()
    }

    fun getPlacesInfoWithoutCurrent(): Flow<List<PlaceWithAQIAndWeather>> {
        return placeDao.getPlacesInfoWithoutCurrent()
    }

    fun getCurrentPlacesInfo(): Flow<PlaceWithAQIAndWeather?>{
        return placeDao.getCurrentPlacesInfo()
    }

    fun getPlacesWithoutCurrent(): Flow<List<Place>> {
        return placeDao.getPlacesWithoutCurrent()
    }

    suspend fun deletePlace(place: Place) {
        placeDao.deletePlace(place = place)
    }

    suspend fun deletePlace(uuid: String) {
        placeDao.deletePlace(uuid = uuid)
    }

    suspend fun deleteAllPlaces() {
        placeDao.deleteAllPlaces()
    }

}