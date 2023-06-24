package ua.airweath.utils.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class LocationManager(private val context: Context) {

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationRequestHigh = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(10000)
        .setMaxUpdateDelayMillis(10000)
        .build()

    private val isLocationEnabled = MutableStateFlow(context.isLocationEnabled())

    lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingPermission")
    private fun locationFlow(looper: Looper? = Looper.myLooper()) =
        callbackFlow {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)



                    locationResult.lastLocation?.let {
                        Timber.i("Last location 1 $it")
                        trySend(it)
                    }
                }

                override fun onLocationAvailability(p0: LocationAvailability) {
                    super.onLocationAvailability(p0)
                    Timber.i("Location availability ${p0.isLocationAvailable}")
                    isLocationEnabled.value = p0.isLocationAvailable
                }
            }

            Timber.i("Starting location updates")

            if (context.hasLocationPermission()) {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).addOnSuccessListener { location ->
                    Timber.i("Last location $location")
                    trySend(location)
                }
                fusedLocationClient.requestLocationUpdates(
                    locationRequestHigh,
                    locationCallback,
                    looper
                ).addOnFailureListener { e ->
                    close(e)
                }
            }

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
                Timber.i("Location update stopped")
            }
        }

    fun getLastLocation(looper: Looper? = Looper.myLooper()): Flow<Location> {
        return locationFlow(looper)
    }

    fun isLocationEnabled(): MutableStateFlow<Boolean> {
        return isLocationEnabled
    }

}