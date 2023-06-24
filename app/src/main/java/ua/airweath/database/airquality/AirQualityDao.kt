package ua.airweath.database.airquality

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AirQualityDao {

    @Upsert
    suspend fun upsertAirQuality(airQuality: AirQuality)

    @Upsert
    suspend fun upsertAirQualities(airQualities: List<AirQuality>)

    @Query("SELECT * FROM air_quality ORDER BY time ASC")
    fun getAirQualities(): Flow<List<AirQuality>>

    @Query("SELECT * FROM air_quality WHERE placeUUID = :placeUUID")
    fun getAirQuality(placeUUID: String): Flow<List<AirQuality>>

    @Query("SELECT * FROM air_quality WHERE id = :id")
    fun getAirQualitiesById(id: String): Flow<List<AirQuality>>

    @Query("SELECT * FROM air_quality WHERE id = :id")
    fun getAirQualityById(id: String): Flow<AirQuality?>

    @Delete
    suspend fun deleteAirQuality(airQuality: AirQuality)

    @Query("DELETE FROM air_quality WHERE placeUUID = :placeUUID AND time LIKE :time || '%'")
    suspend fun deleteAirQuality(placeUUID: String, time: String)

    @Query("DELETE FROM air_quality WHERE time LIKE :time || '%'")
    suspend fun deleteAirQualityByTime(time: String)

    @Query("DELETE FROM air_quality")
    suspend fun deleteAllAirQuality()

}