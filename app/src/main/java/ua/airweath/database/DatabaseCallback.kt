package ua.airweath.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ua.airweath.database.place.Place
import ua.airweath.database.place.PlaceDao
import ua.airweath.enums.CurrentLocationConst
import ua.airweath.usecase.update.IUpdateUseCase
import ua.airweath.usecase.locationbyip.ILocationByIPUseCase
import java.util.UUID
import javax.inject.Provider

class DatabaseCallback(
    private val providerPlaceDao: Provider<PlaceDao>,
    private val iLocationByIPUseCase: Provider<ILocationByIPUseCase>,
    private val iUpdateUseCase: Provider<IUpdateUseCase>,
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        applicationScope.launch(Dispatchers.IO) {
            prepopulatePlace()
        }

    }

    private suspend fun prepopulatePlace() {
        val currentPlace = Place(
            uuid = CurrentLocationConst.CURRENT_LOCATION,
            userId = null,
            formattedAddress = "",
            createdAt = System.currentTimeMillis(),
            favorite = false,
            latitude = 0.0,
            longitude = 0.0
        )
        providerPlaceDao.get().upsertPlace(place = currentPlace)
        iLocationByIPUseCase.get().getLocationByIp().also { results ->
            val formattedAddress = results?.getOrNull(0)?.formattedAddress ?: return
            val location = results.getOrNull(0)?.geometry?.location ?: return

            if (location.lat == null || location.lng == null) return
            val uuid = UUID.randomUUID().toString()
            val place = Place(
                uuid = uuid,
                userId = null,
                formattedAddress = formattedAddress,
                createdAt = System.currentTimeMillis(),
                favorite = true,
                latitude = location.lat,
                longitude = location.lng
            )

            providerPlaceDao.get().upsertPlace(place = place)

            iUpdateUseCase.get().updateAll(LatLng(location.lat, location.lng), uuid)
        }
    }
}