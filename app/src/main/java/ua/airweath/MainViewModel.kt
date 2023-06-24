package ua.airweath

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
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
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.airweath.auth.IGoogleAuth
import ua.airweath.auth.SignInResult
import ua.airweath.database.place.PlaceRepository
import ua.airweath.datastore.ProtoApi
import ua.airweath.enums.CurrentLocationConst.CURRENT_LOCATION
import ua.airweath.usecase.update.IUpdateUseCase
import ua.airweath.utils.location.LocationManager
import ua.airweath.utils.location.isLocationEnabled
import ua.airweath.utils.location.isProviderEnabled
import ua.airweath.workmanagers.DeleteWorker
import ua.airweath.workmanagers.NotificationWorker
import ua.airweath.workmanagers.UpdateWorker
import ua.airweath.workmanagers.Workers
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val protoApi: ProtoApi,
    private val locationManager: LocationManager,
    private val placeRepository: PlaceRepository,
    private val iUpdateUseCase: IUpdateUseCase,
    private val iGoogleAuth: IGoogleAuth,
    workManager: WorkManager,
) : ViewModel() {

    init {
        viewModelScope.launch {
            val now = LocalTime.now()
            val night = LocalTime.MIDNIGHT.minusHours(1)
            val offset = Duration.between(now, night).plusHours(1).seconds
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val updateTime = protoApi.updateTime.first().toLong()
            val notifyTime = protoApi.notifyTime.first().toLong()
            val notifyEnabled = protoApi.notifyEnabled.first()
            val updateWorkerRequest =
                PeriodicWorkRequestBuilder<UpdateWorker>(updateTime, TimeUnit.HOURS, 1, TimeUnit.MINUTES)
                    /*.setInitialDelay(30, TimeUnit.MINUTES)
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 30, TimeUnit.MINUTES)*/
                    .setConstraints(constraints)
                    .build()
            val notifyWorkerRequest =
                PeriodicWorkRequestBuilder<NotificationWorker>(notifyTime, TimeUnit.HOURS, 1, TimeUnit.MINUTES)
                    /*.setInitialDelay(30, TimeUnit.MINUTES)
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 30, TimeUnit.MINUTES)*/
                    .setConstraints(constraints)
                    .build()
            val deleteWorkerRequest = PeriodicWorkRequestBuilder<DeleteWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(offset, TimeUnit.SECONDS)
                .build()
            workManager.enqueueUniquePeriodicWork(
                Workers.UPDATE_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                updateWorkerRequest
            )
            if (notifyEnabled) {
                workManager.enqueueUniquePeriodicWork(
                    Workers.NOTIFICATION_WORKER,
                    ExistingPeriodicWorkPolicy.KEEP,
                    notifyWorkerRequest
                )
            } else {
                workManager.cancelUniqueWork(Workers.NOTIFICATION_WORKER)
            }
            workManager.enqueueUniquePeriodicWork(
                Workers.DELETE_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                deleteWorkerRequest
            )
        }
    }

    val isCurrentLocationUsed = protoApi.isCurrentLocationUsed

    private val _location = mutableStateOf(locationManager.getLastLocation())

    val location = _location.value

    fun startLocationUpdates() {
        _location.value = locationManager.getLastLocation()
    }

    val isLocationEnabled = locationManager.isLocationEnabled()

    fun locationResponse(context: Context, activity: Activity) {
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

    fun updateAll(latLng: LatLng) {
        viewModelScope.launch {
            iUpdateUseCase.updateAll(latLng, CURRENT_LOCATION)
        }
    }

    fun updateByFavoritePlace() {
        viewModelScope.launch {
            placeRepository.getFavoritePlace().firstOrNull().also { placeData ->
                if (placeData == null) return@launch
                val place = placeData.place
                iUpdateUseCase.updateAll(LatLng(place.latitude, place.longitude), place.uuid)
            }
        }
    }

    private val _signInResult = MutableStateFlow(SignInResult())
    val signInResult = _signInResult.asStateFlow()

    private fun onSignInResult(result: SignInResult?) {
        result?.let { res ->
            _signInResult.update {
                it.copy(
                    userData = res.userData,
                    error = res.error
                )
            }
        }
    }

    private fun resetSignInResult() {
        _signInResult.update {
            SignInResult()
        }
    }

    fun signIn(context: Context, block: (intent: IntentSender) -> Unit) {
        viewModelScope.launch {
            val intent = iGoogleAuth.signIn()
            if (intent == null) {
                Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                return@launch
            }
            block(intent)
        }
    }

    fun signInWithIntent(intent: Intent) {
        viewModelScope.launch {
            val signInResult = iGoogleAuth.signInWithIntent(intent = intent)
            onSignInResult(signInResult)
        }
    }

    fun getSignedInUser() {
        viewModelScope.launch {
            iGoogleAuth.getSignedInUser()?.let {
                onSignInResult(result = SignInResult(userData = it))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            iGoogleAuth.signOut()
            resetSignInResult()
        }
    }

}