package ua.airweath.auth

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import ua.airweath.BuildConfig
import ua.airweath.database.place.Place
import ua.airweath.database.place.PlaceRepository
import ua.airweath.usecase.update.IUpdateUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuth @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val signInClient: SignInClient,
    private val placeRepository: PlaceRepository,
    private val iUpdateUseCase: IUpdateUseCase,
) : IGoogleAuth {

    override suspend fun signIn(): IntentSender? {
        val result = try {
            signInClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = signInClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val result = auth.signInWithCredential(googleCredentials).await()
            if (result.additionalUserInfo?.isNewUser == true) {
                CoroutineScope(Dispatchers.IO).launch {
                    placeRepository.getPlacesWithoutCurrent().firstOrNull()?.let { places ->
                        places.forEach { place ->
                            firestore.collection("places")
                                .document(place.uuid)
                                .set(place.copy(userId = result.user?.uid))
                                .addOnSuccessListener {
                                    Timber.d("${place.uuid} added success")
                                }
                                .addOnFailureListener {
                                    Timber.d("${place.uuid} failure $it")
                                }
                        }
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val places = firestore.collection("places")
                        .whereEqualTo("userId", result.user?.uid)
                        .get()
                        .addOnSuccessListener { result ->
                            Timber.d("Success get ${result.size()}")
                        }
                        .addOnFailureListener {
                            Timber.d("Failure get $it")
                        }
                        .await().documents.mapNotNull { doc ->
                            val uuid = doc.id
                            val userId = doc.getString("userId")
                            val formattedAddress = doc.getString("formattedAddress")
                            val createdAt = doc.getLong("createdAt")
                            val favorite = doc.getBoolean("favorite")
                            val latitude = doc.getDouble("latitude")
                            val longitude = doc.getDouble("longitude")

                            if (formattedAddress == null || createdAt == null || favorite == null
                                || latitude == null || longitude == null
                            ) {
                                null
                            } else {
                                Place(
                                    uuid = uuid,
                                    userId = userId,
                                    formattedAddress = formattedAddress,
                                    createdAt = createdAt,
                                    favorite = favorite,
                                    latitude = latitude,
                                    longitude = longitude
                                )
                            }
                        }
                    val favoritePlace = placeRepository.getFavoritePlace().firstOrNull()?.place
                    places.firstOrNull { it.favorite }?.uuid?.let { dbFavoriteId ->
                        if (favoritePlace?.uuid != dbFavoriteId) {
                            favoritePlace?.copy(favorite = false)
                                ?.let { placeRepository.upsertPlace(it) }
                        }
                    }
                    val dbPlaces = placeRepository.getPlacesWithoutCurrent().firstOrNull()?.filter { it.userId.isNullOrBlank() }
                    dbPlaces?.forEach { place ->
                        placeRepository.upsertPlace(place.copy(userId = result.user?.uid))
                    }
                    placeRepository.upsertPlaces(places)
                    places.forEach { place ->
                        iUpdateUseCase.updateAll(LatLng(place.latitude, place.longitude), place.uuid)
                    }
                    placeRepository.getPlacesWithoutCurrent().firstOrNull()?.forEach { place ->
                        if (places.any { it.uuid != place.uuid }) {
                            firestore.collection("places")
                                .document(place.uuid)
                                .set(place.copy(userId = result.user?.uid))
                                .addOnSuccessListener {
                                    Timber.d("${place.uuid} added success")
                                }
                                .addOnFailureListener {
                                    Timber.d("${place.uuid} failure $it")
                                }
                        }
                    }
                }
            }
            SignInResult(
                userData = result.user?.run {
                    UserData(
                        id = uid,
                        username = displayName,
                    )
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                error = e.message
            )
        }
    }

    override suspend fun signOut() {
        try {
            signInClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    override fun getSignedInUser(): UserData? {
        return auth.currentUser?.run {
            UserData(
                id = uid,
                username = displayName,
            )
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.webClientId)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

}