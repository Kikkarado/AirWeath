package ua.airweath.database.place

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ua.airweath.database.relations.PlaceWithAQIAndWeather

@Dao
interface PlaceDao {

    @Upsert
    suspend fun upsertPlace(place: Place)

    @Upsert
    suspend fun upsertPlaces(places: List<Place>)

    @Transaction
    @Query("SELECT * FROM places ORDER BY favorite ASC, createdAt DESC")
    fun getPlaces(): Flow<List<PlaceWithAQIAndWeather>>

    @Transaction
    @Query("SELECT * FROM places WHERE uuid != 'current_location' ORDER BY favorite DESC, createdAt DESC")
    fun getPlacesInfoWithoutCurrent(): Flow<List<PlaceWithAQIAndWeather>>

    @Transaction
    @Query("SELECT * FROM places WHERE uuid == 'current_location' LIMIT 1")
    fun getCurrentPlacesInfo(): Flow<PlaceWithAQIAndWeather?>

    @Query("SELECT * FROM places WHERE uuid != 'current_location' ORDER BY favorite DESC, createdAt DESC")
    fun getPlacesWithoutCurrent(): Flow<List<Place>>

    @Transaction
    @Query("SELECT * FROM places WHERE uuid = :uuid LIMIT 1")
    fun getPlace(uuid: String): Flow<PlaceWithAQIAndWeather?>

    @Transaction
    @Query("SELECT * FROM places WHERE favorite = 1 LIMIT 1")
    fun getFavoritePlace(): Flow<PlaceWithAQIAndWeather?>

    @Delete
    suspend fun deletePlace(place: Place)

    @Query("DELETE FROM places WHERE uuid = :uuid")
    suspend fun deletePlace(uuid: String)

    @Query("DELETE FROM places")
    suspend fun deleteAllPlaces()

}