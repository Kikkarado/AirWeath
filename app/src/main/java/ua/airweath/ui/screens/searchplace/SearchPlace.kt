package ua.airweath.ui.screens.searchplace

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddLocation
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import timber.log.Timber
import ua.airweath.R
import ua.airweath.ui.AppState
import ua.airweath.ui.components.AppButton
import ua.airweath.ui.components.AppDropDownPlaces
import ua.airweath.ui.components.AppTextField
import ua.airweath.ui.components.Placeholder
import ua.airweath.ui.components.TopAppBar
import ua.airweath.ui.components.WaitingDialog
import ua.airweath.ui.theme.spacing

@OptIn(ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchPlace(
    appState: AppState,
    latLng: LatLng,
    searchPlaceViewModel: SearchPlaceViewModel = hiltViewModel(),
) {
    val searchingPlace by searchPlaceViewModel.searchingPlace
    val addresses = searchPlaceViewModel.addresses
    val place by searchPlaceViewModel.place
    val showWaitingDialog by searchPlaceViewModel.showWaitingDialog
    val permissionsState =
        rememberMultiplePermissionsState(
            permissions =
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Manifest.permission.ACCESS_BACKGROUND_LOCATION else null,
            ).mapNotNull { it }
        )
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = stringResource(id = R.string.search_place)) {
                appState.navController.navigateUp()
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            var isMapLoaded by remember { mutableStateOf(false) }
            val mapProperties by remember(permissionsState) {
                mutableStateOf(
                    MapProperties(
                        isMyLocationEnabled = permissionsState.permissions.any { it.status.isGranted },
                        mapType = MapType.NORMAL,
                        minZoomPreference = 3f
                    )
                )
            }
            val uiProperties by remember {
                mutableStateOf(
                    MapUiSettings(
                        compassEnabled = true,
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = true,
                        mapToolbarEnabled = true,
                        rotationGesturesEnabled = true,
                    )
                )
            }
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(latLng, 18f)
            }
            LaunchedEffect(place) {
                place?.let {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(it.latitude, it.longitude), 18f)
                }
            }
            GoogleMap(
                modifier = Modifier
                    .matchParentSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    isMapLoaded = true
                },
                properties = mapProperties,
                uiSettings = uiProperties,
            )
            if (!isMapLoaded) {
                AnimatedVisibility(
                    modifier = Modifier
                        .matchParentSize(),
                    visible = !isMapLoaded,
                    enter = EnterTransition.None,
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .wrapContentSize()
                    )
                }
            }
            Box(
                modifier = Modifier
                    .matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .size(36.dp)
                        .offset(y = (-18).dp)
                )
            }
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(MaterialTheme.spacing.large),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                var expanded by remember {
                    mutableStateOf(false)
                }
                AppDropDownPlaces(
                    modifier = Modifier
                        .fillMaxWidth(.8f)
                        .height(50.dp),
                    options = addresses,
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    onDismissRequest = { expanded = false},
                    onClick = { id, option ->
                        searchPlaceViewModel.setSearchingPlace(TextFieldValue(text = option, selection = TextRange(option.length)))
                        searchPlaceViewModel.setPlaceData(option)
                    },
                    readOnly = false,
                    value = searchingPlace,
                    onValueChange = {
                        searchPlaceViewModel.setSearchingPlace(it)
                    },
                    placeholder = {
                        Placeholder(value = stringResource(id = R.string.search))
                    },
                    trailingIconClick = {
                        searchPlaceViewModel.clearPlaceData()
                    }
                )
                AppButton(
                    onClick = {
                        searchPlaceViewModel.addPlace {
                            appState.navController.navigateUp()
                        }
                              },
                    text = stringResource(id = R.string.add_place),
                    modifier = Modifier
                )
            }
        }
    }
    if (showWaitingDialog) WaitingDialog()
}