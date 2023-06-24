package ua.airweath.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ua.airweath.database.airquality.AirQuality
import ua.airweath.database.place.Place
import ua.airweath.database.weather.Weather

data class PlaceWithAQIAndWeather(
    @Embedded val place: Place,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "placeUUID",
    )
    val aqiQualities: List<AirQuality>,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "placeUUID"
    )
    val weathers: List<Weather>,
)
