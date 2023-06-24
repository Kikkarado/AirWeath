package ua.airweath.workmanagers

import android.content.Context
import android.os.Looper
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.maps.model.LatLng
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import ua.airweath.database.airquality.AirQualityRepository
import ua.airweath.database.place.PlaceRepository
import ua.airweath.datastore.ProtoApi
import ua.airweath.enums.CurrentLocationConst
import ua.airweath.usecase.update.IUpdateUseCase
import ua.airweath.utils.location.LocationManager
import ua.airweath.utils.location.isProviderEnabled

@HiltWorker
class UpdateWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val placeRepository: PlaceRepository,
    private val airQualityRepository: AirQualityRepository,
    private val iUpdateUseCase: IUpdateUseCase,
    private val locationManager: LocationManager,
    private val protoApi: ProtoApi,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Timber.d("Start worker")
        val aqiType = protoApi.aqiType.first()
        Timber.d("aqiType $aqiType")
        val isCurrentLocationUsed = protoApi.isCurrentLocationUsed.first()
        Timber.d("isCurrentLocationUsed $isCurrentLocationUsed")
        val location = locationManager.getLastLocation(Looper.getMainLooper()).firstOrNull()

        if (isCurrentLocationUsed && context.isProviderEnabled()) {
            if (location != null) {
                Timber.d("location ${location.latitude} ${location.longitude}")
                iUpdateUseCase.updateAll(
                    LatLng(location.latitude, location.longitude),
                    CurrentLocationConst.CURRENT_LOCATION
                )
            }
        }

        placeRepository.getPlacesWithoutCurrent().first().forEach { place ->
            iUpdateUseCase.updateAll(LatLng(place.latitude, place.longitude), place.uuid)
        }
        return Result.success()
    }
}