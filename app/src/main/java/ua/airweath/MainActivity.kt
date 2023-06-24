package ua.airweath

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.res.Configuration
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import ua.airweath.auth.SignInResult
import ua.airweath.database.place.PlaceRepository
import ua.airweath.datastore.ProtoApi
import ua.airweath.di.ProtoEntryPoint
import ua.airweath.receiver.GpsSettingsReceiver
import ua.airweath.ui.Main
import ua.airweath.ui.rememberAppState
import ua.airweath.ui.theme.AirWeathTheme
import ua.airweath.utils.ConnectionState
import ua.airweath.utils.connectivityState
import ua.airweath.utils.locale.ContextUtils
import ua.airweath.utils.location.isProviderEnabled
import java.time.Duration
import java.time.LocalTime
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var protoApi: ProtoApi

    @Inject
    lateinit var placeRepository: PlaceRepository

    private lateinit var gpsSettingsReceiver: GpsSettingsReceiver

    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSignedInUser()

        gpsSettingsReceiver = GpsSettingsReceiver(this)
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(gpsSettingsReceiver, filter)

        setContent {

            val location by
            viewModel.location.collectAsStateWithLifecycle(initialValue = null)

            val connectionState by connectivityState()

            val isCurrentLocationUsed by viewModel.isCurrentLocationUsed.collectAsStateWithLifecycle(
                initialValue = null
            )
            val isLocationEnabled by viewModel.isLocationEnabled.collectAsStateWithLifecycle(
                initialValue = applicationContext.isProviderEnabled()
            )

            LaunchedEffect(location) {
                println("location $location")
            }

            LaunchedEffect(isCurrentLocationUsed, isLocationEnabled, connectionState) {
                Timber.d("Get new AQI")
                Timber.d("isCurrentLocationUsed $isCurrentLocationUsed")
                if (connectionState == ConnectionState.Unavailable) return@LaunchedEffect
                if (isCurrentLocationUsed == true) {
                    Timber.d("isLocationEnabled $isLocationEnabled")
                    if (isLocationEnabled) {
                        viewModel.startLocationUpdates()
                        val loc = viewModel.location.firstOrNull()
                        viewModel.updateAll(
                            LatLng(
                                location?.latitude ?: loc?.latitude ?: .0,
                                location?.longitude ?: loc?.longitude ?: .0
                            )
                        )
                    } else {
                        viewModel.updateByFavoritePlace()
                    }
                } else {
                    viewModel.updateByFavoritePlace()
                }
            }

            val permissionsState = rememberMultiplePermissionsState(
                permissions = listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Manifest.permission.ACCESS_BACKGROUND_LOCATION else null,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.POST_NOTIFICATIONS else null,
                ).mapNotNull { it }
            )

            LaunchedEffect(Unit) {
                if (protoApi.isFirstStart.first()) {
                    permissionsState.launchMultiplePermissionRequest()
                }
                if (viewModel.isCurrentLocationUsed.first())
                    viewModel.locationResponse(this@MainActivity, this@MainActivity)
            }

            LaunchedEffect(
                permissionsState.permissions[0].status.isGranted &&
                        permissionsState.permissions[1].status.isGranted
            ) {
                if (protoApi.isFirstStart.first() &&
                    permissionsState.permissions[0].status.isGranted &&
                    permissionsState.permissions[1].status.isGranted
                ) {
                    protoApi.setIsCurrentLocationUsed(true)
                    protoApi.setIsFirstStart(false)
                }
            }

            AirWeathTheme {
                Main(
                    appState = rememberAppState(),
                    viewModel = viewModel
                )
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) return super.attachBaseContext(null)
        val protoApi =
            EntryPointAccessors.fromApplication(newBase, ProtoEntryPoint::class.java).protoApi
        val localeToSwitch = Locale(runBlocking { protoApi.locale.first() })
        val localeUpdatedContext = ContextUtils.updateLocale(newBase, localeToSwitch)
        super.attachBaseContext(localeUpdatedContext)

    }

    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(gpsSettingsReceiver)
        } catch (_: Exception) {
        }
        runBlocking {
            if (protoApi.isFirstStart.first()) {
                protoApi.setIsFirstStart(false)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AirWeathTheme {
    }
}