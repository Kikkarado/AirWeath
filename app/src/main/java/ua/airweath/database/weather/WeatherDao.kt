package ua.airweath.database.weather

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Upsert
    suspend fun upsertWeather(weather: Weather)

    @Upsert
    suspend fun upsertWeathers(weathers: List<Weather>)

    @Query("SELECT * FROM weather ORDER BY time ASC")
    fun getWeathers(): Flow<List<Weather>>

    @Query("SELECT * FROM weather WHERE placeUUID = :placeUUID")
    fun getWeather(placeUUID: String): Flow<List<Weather>>

    @Query("SELECT * FROM weather WHERE id = :id")
    fun getWeathersById(id: String): Flow<List<Weather>>

    @Query("SELECT * FROM air_quality WHERE id = :id")
    fun getWeatherById(id: String): Flow<Weather?>

    @Delete
    suspend fun deleteWeather(weather: Weather)

    @Query("DELETE FROM weather WHERE placeUUID = :placeUUID AND time LIKE :time || '%'")
    suspend fun deleteWeather(placeUUID: String, time: String)

    @Query("DELETE FROM air_quality WHERE time LIKE :time || '%'")
    suspend fun deleteWeatherByTime(time: String)

    @Query("DELETE FROM weather")
    suspend fun deleteAllWeathers()

}