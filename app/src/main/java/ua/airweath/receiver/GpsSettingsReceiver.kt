package ua.airweath.receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import ua.airweath.utils.location.LocationManager
import ua.airweath.utils.location.isLocationEnabled
import ua.airweath.utils.location.isProviderEnabled
import javax.inject.Inject

@AndroidEntryPoint
class GpsSettingsReceiver(
    private val activity: Activity
): BroadcastReceiver() {

    @Inject lateinit var locationManager: LocationManager

    private var isCalled = false

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == android.location.LocationManager.PROVIDERS_CHANGED_ACTION) {
            if (context != null) locationUpdatesListener(context, activity)
            isCalled = true
        }
    }

    private fun locationUpdatesListener(context: Context, activity: Activity) {
        val isLocationEnabled = context.isLocationEnabled()
        val isProviderEnabled = context.isProviderEnabled()
        Timber.i("isLocationEnabled $isLocationEnabled")
        Timber.i("GPS_PROVIDER $isProviderEnabled")

        if (isLocationEnabled) isCalled = false

        if (isCalled) return
        val settingsClient = LocationServices.getSettingsClient(context)
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationManager.locationRequestHigh)
        val response = settingsClient.checkLocationSettings(builder.setAlwaysShow(true).build())

        response.addOnSuccessListener {
            it.locationSettingsStates?.also { state ->
                Timber.i("isGpsUsable ${state.isGpsUsable}")
                Timber.i("isBleUsable ${state.isBleUsable}")
                Timber.i("isLocationUsable ${state.isLocationUsable}")
                Timber.i("isNetworkLocationUsable ${state.isNetworkLocationUsable}")
                Timber.i("isGpsPresent ${state.isGpsPresent}")
                Timber.i("isBlePresent ${state.isBlePresent}")
                Timber.i("isLocationPresent ${state.isLocationPresent}")
                Timber.i("isNetworkLocationPresent ${state.isNetworkLocationPresent}")
            }
            isCalled = false
        }


        response.addOnFailureListener { e ->
            Timber.i("Location failure $e")
            if (e is ResolvableApiException) {
                try {
                    // Handle result in onActivityResult()
                    e.startResolutionForResult(
                        activity,
                        999
                    )
                } catch (_: IntentSender.SendIntentException) {
                }
            }
        }

    }

}