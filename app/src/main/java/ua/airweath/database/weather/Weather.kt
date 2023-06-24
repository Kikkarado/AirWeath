package ua.airweath.database.weather

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ua.airweath.database.place.Place

@Entity(
    tableName = "weather",
    foreignKeys = [
        ForeignKey(
            entity = Place::class,
            parentColumns = ["uuid"],
            childColumns = ["placeUUID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Weather(
    val time: String,
    @ColumnInfo(index = true)
    val placeUUID: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = "$time $placeUUID",
    val temperature2m: Float? = null,
    val relativehumidity2m: Int? = null,
    val apparentTemperature: Float? = null,
    val precipitation: Float? = null,
    val rain: Float? = null,
    val weathercode: Int? = null,
    val windspeed10m: Float? = null,
    val windgusts10m: Float? = null,
)
