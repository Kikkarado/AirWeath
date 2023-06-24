package ua.airweath.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.airweath.database.airquality.AirQuality
import ua.airweath.database.airquality.AirQualityDao
import ua.airweath.database.place.Place
import ua.airweath.database.place.PlaceDao
import ua.airweath.database.weather.Weather
import ua.airweath.database.weather.WeatherDao

@Database(
    entities = [
        Place::class,
        AirQuality::class,
        Weather::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RoomDB : RoomDatabase() {

    abstract fun placeDao(): PlaceDao

    abstract fun airQualityDao(): AirQualityDao

    abstract fun weatherDao(): WeatherDao

}