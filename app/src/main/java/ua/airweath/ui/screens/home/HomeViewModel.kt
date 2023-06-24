package ua.airweath.ui.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ua.airweath.data.airquality.AirQualityInfo
import ua.airweath.database.airquality.AirQuality
import ua.airweath.database.airquality.AirQualityRepository
import ua.airweath.database.place.PlaceRepository
import ua.airweath.datastore.ProtoApi
import ua.airweath.ktor.ServiceApi
import ua.airweath.utils.location.LocationManager
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val serviceApi: ServiceApi,
    private val placeRepository: PlaceRepository,
    private val protoApi: ProtoApi,
    private val locationManager: LocationManager,
    private val airQualityRepository: AirQualityRepository,
) : ViewModel() {

    val isCurrentLocationUsed = protoApi.isCurrentLocationUsed

    val isLocationEnabled = locationManager.isLocationEnabled()

    private val _location = mutableStateOf(locationManager.getLastLocation())

    val location = _location.value

    fun startLocationUpdates() {
        _location.value = locationManager.getLastLocation()
    }

    private val _airQualityInfo = mutableStateOf<AirQualityInfo?>(null)
    val airQualityInfo: State<AirQualityInfo?> get() = _airQualityInfo

    private val _placeUUID = mutableStateOf("")
    val placeUUID: State<String> get() = _placeUUID

    fun setPlaceUUID(placeUUID: String) {
        _placeUUID.value = placeUUID
    }

    val favoritePlace = placeRepository.getFavoritePlace()

    fun getFlowAirQuality(placeUUID: String): Flow<List<AirQuality>> {
        return airQualityRepository.getAirQualitiesByPlaceUUID(placeUUID)
    }
    
    fun getPlace(placeUUID: String) = placeRepository.getPlace(placeUUID)

    val aqiType = protoApi.aqiType

    private val _showAboutDialog = mutableStateOf(false)
    val showAboutDialog: State<Boolean> get() = _showAboutDialog

    private val _textAboutDialog = mutableStateOf("")
    val textAboutDialog: State<String> get() = _textAboutDialog

    fun openAboutDialog(text: String) {
        _textAboutDialog.value = text
        _showAboutDialog.value = true
    }

    fun closeAboutDialog(){
        _textAboutDialog.value = ""
        _showAboutDialog.value = false
    }

    private val _showRecommendationDialog = mutableStateOf(false)
    val showRecommendationDialog: State<Boolean> get() = _showRecommendationDialog

    private val _textRecommendationDialog = mutableStateOf("")
    val textRecommendationDialog: State<String> get() = _textRecommendationDialog

    fun openRecommendationDialog(text: String) {
        _textRecommendationDialog.value = text
        _showRecommendationDialog.value = true
    }

    fun closeRecommendationDialog(){
        _textRecommendationDialog.value = ""
        _showRecommendationDialog.value = false
    }

}