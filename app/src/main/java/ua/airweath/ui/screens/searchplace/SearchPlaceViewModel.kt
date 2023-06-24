package ua.airweath.ui.screens.searchplace

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.text.style.StyleSpan
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.airweath.BuildConfig
import ua.airweath.auth.IGoogleAuth
import ua.airweath.database.place.Place
import ua.airweath.database.place.PlaceRepository
import ua.airweath.datastore.ProtoApi
import ua.airweath.usecase.update.IUpdateUseCase
import java.io.IOException
import java.util.Locale
import java.util.UUID
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class SearchPlaceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val placeRepository: PlaceRepository,
    private val protoApi: ProtoApi,
    private val iGoogleAuth: IGoogleAuth,
    private val iUpdateUseCase: IUpdateUseCase,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    private lateinit var placesClient: PlacesClient

    init {
        viewModelScope.launch {
            Places.initialize(context, BuildConfig.apiKey, Locale(protoApi.locale.first()))
            placesClient = Places.createClient(context)
        }
    }

    private val _searchingPlace = mutableStateOf(TextFieldValue(text = "", selection = TextRange.Zero))
    val searchingPlace: State<TextFieldValue> get() = _searchingPlace

    private val _place = mutableStateOf<Place?>(null)
    val place: State<Place?> get() = _place

    private val _showWaitingDialog = mutableStateOf(false)
    val showWaitingDialog: State<Boolean> get() = _showWaitingDialog

    fun setSearchingPlace(text: TextFieldValue) {
        _searchingPlace.value = text
        getAddressList()
    }

    fun setPlaceData(address: String) {
        val latLng = getLocationFromAddress(context = context, address) ?: return
        _place.value = Place(
            uuid = UUID.randomUUID().toString(),
            userId = iGoogleAuth.getSignedInUser()?.id,
            formattedAddress = address,
            createdAt = System.currentTimeMillis(),
            favorite = false,
            latitude = latLng.latitude,
            longitude = latLng.longitude
        )
    }

    fun clearPlaceData() {
        _place.value = null
    }

    fun addPlace(ifSuccess: () -> Unit) {
        _showWaitingDialog.value = true
        viewModelScope.launch {
            _place.value?.let {
                placeRepository.upsertPlace(it)
                if (iGoogleAuth.getSignedInUser()?.id != null) {
                    firestore.collection("places")
                        .document(it.uuid)
                        .set(it)
                        .addOnSuccessListener { _ ->
                            Timber.d("${it.uuid} added success")
                        }
                        .addOnFailureListener { e ->
                            Timber.d("${it.uuid} failure $e")
                        }
                }
                iUpdateUseCase.updateAll(LatLng(it.latitude, it.longitude), it.uuid)
                    .also { res ->
                        if (!res.first.isNullOrEmpty() && !res.second.isNullOrEmpty()) {
                            _showWaitingDialog.value = false
                            ifSuccess()
                        }
                    }
            }
        }
    }

    private val _addresses = mutableStateListOf<String>()
    val addresses: List<String> get() = _addresses

    private fun getAddressList() {
        viewModelScope.launch(Dispatchers.IO) {
            getAutocomplete(
                placesClient,
                _searchingPlace.value.text,
            ).also { list ->
                _addresses.clear()
                val mapList = list.map {
                    it.getFullText(
                        StyleSpan(Typeface.NORMAL)
                    ).toString()
                }
                _addresses.addAll(mapList)
                if (mapList.size == 1) {
                    setPlaceData(mapList[0])
                    _searchingPlace.value = TextFieldValue(text = mapList[0], selection = TextRange(mapList[0].length))
                }
            }
        }
    }

    private fun getAutocomplete(
        mPlacesClient: PlacesClient,
        constraint: String,
    ): List<AutocompletePrediction> {

        val bounds = RectangularBounds.newInstance(
            LatLng(-33.880490, 151.184363),
            LatLng(-33.858754, 151.229596)
        )

        var list = listOf<AutocompletePrediction>()
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
            .setTypesFilter(listOf(PlaceTypes.GEOCODE))
            .setLocationBias(bounds)
            .setSessionToken(token)
            .setQuery(constraint)
            .build()
        val prediction = mPlacesClient.findAutocompletePredictions(request)
        try {
            Tasks.await(prediction, 120, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }

        if (prediction.isSuccessful) {
            val findAutocompletePredictionsResponse = prediction.result
            findAutocompletePredictionsResponse?.let {
                list = findAutocompletePredictionsResponse.autocompletePredictions
            }
            return list
        }
        return list
    }

    private fun getLocationFromAddress(context: Context, strAddress: String): LatLng? {
        val coder = Geocoder(context)
        var address: List<Address>? = null
        var latLng: LatLng? = null
        try {
            if (Build.VERSION.SDK_INT >= 33) {
                val geocodeListener = Geocoder.GeocodeListener { addresses ->
                    address = addresses
                }
                coder.getFromLocationName(strAddress, 15, geocodeListener)
            } else {
                @Suppress("DEPRECATION")
                address = coder.getFromLocationName(strAddress, 15)
            }
            val location: Address = address?.get(0) ?: return null
            latLng = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return latLng
    }

}