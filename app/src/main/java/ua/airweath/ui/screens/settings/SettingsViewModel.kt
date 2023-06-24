package ua.airweath.ui.screens.settings

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import ua.airweath.datastore.ProtoApi
import ua.airweath.utils.findActivity
import ua.airweath.utils.location.LocationManager
import ua.airweath.utils.location.isLocationEnabled
import ua.airweath.utils.location.isProviderEnabled
import ua.airweath.workmanagers.NotificationWorker
import ua.airweath.workmanagers.UpdateWorker
import ua.airweath.workmanagers.Workers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val protoApi: ProtoApi,
    private val workManager: WorkManager,
    private val locationManager: LocationManager,
): ViewModel() {

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun setLocale(locale: String) {
        runBlocking {
            protoApi.setLocale(locale)
        }
    }

    val isCurrentLocationUsed = protoApi.isCurrentLocationUsed

    fun changeCurrentLocationUsed(isUsed: Boolean, context: Context) {
        viewModelScope.launch {
            protoApi.setIsCurrentLocationUsed(isUsed)
            if (isUsed) {
                locationUpdatesListener(context, context.findActivity())
            }
        }
    }

    val aqiType = protoApi.aqiType

    fun setAQIType(type: String) {
        viewModelScope.launch {
            protoApi.setAqiType(type)
        }
    }

    val updateInterval = protoApi.updateTime

    fun setUpdateTime(interval: Int) {
        viewModelScope.launch {
            val updateWorkerRequest =
                PeriodicWorkRequestBuilder<UpdateWorker>(interval.toLong(), TimeUnit.HOURS, 1, TimeUnit.MINUTES)
                    /*.setInitialDelay(30, TimeUnit.MINUTES)
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 30, TimeUnit.MINUTES)*/
                    .setConstraints(constraints)
                    .build()
            protoApi.setUpdateTime(interval)
            workManager.cancelUniqueWork(Workers.UPDATE_WORKER)
            workManager.enqueueUniquePeriodicWork(
                Workers.UPDATE_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                updateWorkerRequest
            )
        }
    }

    val notifyEnabled = protoApi.notifyEnabled

    fun setNotifyEnabled(enabled: Boolean) {
        viewModelScope.launch {
            protoApi.setNotifyEnabled(enabled)
            if (enabled) {
                val interval = protoApi.notifyTime.first()
                val notifyWorkerRequest =
                    PeriodicWorkRequestBuilder<NotificationWorker>(interval.toLong(), TimeUnit.HOURS, 1, TimeUnit.MINUTES)
                        /*.setInitialDelay(30, TimeUnit.MINUTES)
                        .setBackoffCriteria(BackoffPolicy.LINEAR, 30, TimeUnit.MINUTES)*/
                        .setConstraints(constraints)
                        .build()
                workManager.enqueueUniquePeriodicWork(
                    Workers.NOTIFICATION_WORKER,
                    ExistingPeriodicWorkPolicy.KEEP,
                    notifyWorkerRequest
                )
            } else {
                workManager.cancelUniqueWork(Workers.NOTIFICATION_WORKER)
            }
        }
    }

    val notifyInterval = protoApi.notifyTime

    fun setNotifyTime(interval: Int) {
        viewModelScope.launch {
            val notifyWorkerRequest =
                PeriodicWorkRequestBuilder<NotificationWorker>(interval.toLong(), TimeUnit.HOURS, 1, TimeUnit.MINUTES)
                    /*.setInitialDelay(30, TimeUnit.MINUTES)
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 30, TimeUnit.MINUTES)*/
                    .setConstraints(constraints)
                    .build()
            protoApi.setNotifyTime(interval)
            workManager.cancelUniqueWork(Workers.NOTIFICATION_WORKER)
            workManager.enqueueUniquePeriodicWork(
                Workers.NOTIFICATION_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                notifyWorkerRequest
            )
        }
    }

    private fun locationUpdatesListener(context: Context, activity: Activity) {
        val isLocationEnabled = context.isLocationEnabled()
        val isProviderEnabled = context.isProviderEnabled()
        Timber.i("isLocationEnabled $isLocationEnabled")
        Timber.i("GPS_PROVIDER $isProviderEnabled")

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