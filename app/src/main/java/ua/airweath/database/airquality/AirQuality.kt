package ua.airweath.database.airquality

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ua.airweath.database.place.Place

@Entity(
    tableName = "air_quality",
    foreignKeys = [
        ForeignKey(
            entity = Place::class,
            parentColumns = ["uuid"],
            childColumns = ["placeUUID"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AirQuality(
    val time: String,
    @ColumnInfo(index = true)
    val placeUUID: String,
    @PrimaryKey(autoGenerate = false)
    val id: String = "$time $placeUUID",
    val pm10: Float? = null,
    val pm25: Float? = null,
    val carbonMonoxide: Float? = null,
    val nitrogenDioxide: Float? = null,
    val sulphurDioxide: Float? = null,
    val ozone: Float? = null,
    val dust: Float? = null,
    val ammonia: Float? = null,
    val europeanAqi: Int? = null,
    val europeanAqiPm25: Int? = null,
    val europeanAqiPm10: Int? = null,
    val europeanAqiNo2: Int? = null,
    val europeanAqiO3: Int? = null,
    val europeanAqiSo2: Int? = null,
    val usAqi: Int? = null,
    val usAqiPm25: Int? = null,
    val usAqiPm10: Int? = null,
    val usAqiNo2: Int? = null,
    val usAqiCo: Int? = null,
    val usAqiO3: Int? = null,
    val usAqiSo2: Int? = null,
)
