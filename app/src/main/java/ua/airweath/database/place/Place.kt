package ua.airweath.database.place

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
    @PrimaryKey
    val uuid: String,
    val userId: String?,
    val formattedAddress: String,
    val createdAt: Long,
    val favorite: Boolean,
    val latitude: Double,
    val longitude: Double,
)
