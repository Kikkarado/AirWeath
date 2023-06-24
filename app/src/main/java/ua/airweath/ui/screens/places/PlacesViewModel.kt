package ua.airweath.ui.screens.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.airweath.auth.IGoogleAuth
import ua.airweath.database.place.Place
import ua.airweath.database.place.PlaceRepository
import ua.airweath.datastore.ProtoApi
import ua.airweath.enums.AqiTypes
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    protoApi: ProtoApi,
    private val iGoogleAuth: IGoogleAuth,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    val aqiType = protoApi.aqiType.distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), AqiTypes.UsAQI.type)

    val places = placeRepository.getPlacesInfoWithoutCurrent().distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun deletePlace(uuid: String) {
        viewModelScope.launch {
            placeRepository.deletePlace(uuid)
            if (iGoogleAuth.getSignedInUser()?.id != null) {
                firestore.collection("places")
                    .document(uuid)
                    .delete()
                    .addOnSuccessListener {
                        Timber.d("$uuid deleted success")
                    }
                    .addOnFailureListener { e ->
                        Timber.d("$uuid failure $e")
                    }
            }
        }
    }

    fun changeFavorite(place: Place) {
        viewModelScope.launch {
            val oldFavorite = placeRepository.getFavoritePlace().firstOrNull()?.place
            placeRepository.upsertPlace(place.copy(favorite = true))
            if (iGoogleAuth.getSignedInUser()?.id != null) {
                firestore.collection("places")
                    .document(place.uuid)
                    .update("favorite", true)
                    .addOnSuccessListener {
                        Timber.d("${place.uuid} updated success")
                    }
                    .addOnFailureListener { e ->
                        Timber.d("${place.uuid} failure $e")
                    }
            }
            if (oldFavorite != null) {
                placeRepository.upsertPlace(oldFavorite.copy(favorite = false))
                if (iGoogleAuth.getSignedInUser()?.id != null) {
                    firestore.collection("places")
                        .document(oldFavorite.uuid)
                        .update("favorite", false)
                        .addOnSuccessListener {
                            Timber.d("${oldFavorite.uuid} updated success")
                        }
                        .addOnFailureListener { e ->
                            Timber.d("${oldFavorite.uuid} failure $e")
                        }
                }
            }
        }
    }

}