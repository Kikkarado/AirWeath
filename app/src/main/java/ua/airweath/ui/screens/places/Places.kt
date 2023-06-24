package ua.airweath.ui.screens.places

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddLocation
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import ua.airweath.R
import ua.airweath.data.mappers.toAirQualityInfo
import ua.airweath.data.mappers.toWeatherInfo
import ua.airweath.data.weather.WeatherType
import ua.airweath.database.place.Place
import ua.airweath.database.relations.PlaceWithAQIAndWeather
import ua.airweath.enums.AqiTypes
import ua.airweath.ui.AppState
import ua.airweath.ui.components.Caption
import ua.airweath.ui.components.Panel
import ua.airweath.ui.components.TopAppBar
import ua.airweath.ui.navigation.NavRoutes
import ua.airweath.ui.theme.StarDust
import ua.airweath.ui.theme.spacing

@Composable
fun Places(
    appState: AppState,
    placesViewModel: PlacesViewModel = hiltViewModel(),
) {

    val aqiType by placesViewModel.aqiType.collectAsStateWithLifecycle()
    val places by placesViewModel.places.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = stringResource(id = R.string.my_places)) {
                appState.navController.navigateUp()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val latLng = places[0].place.let {
                        LatLng(it.latitude, it.longitude)
                    }
                    appState.navController.navigate(
                        NavRoutes.SearchPlace(
                            latitude = latLng.latitude.toString(),
                            longitude = latLng.longitude.toString()
                        ).route
                    ) {
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.AddLocation,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .matchParentSize(),
                contentPadding = PaddingValues(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                items(places) { place ->
                    PlaceItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        place = place,
                        aqiType = aqiType,
                        onClick = { uuid ->
                            appState.navController.navigate(NavRoutes.Home(uuid).route) {
                                launchSingleTop = true
                            }
                        },
                        changeFavorite = { p0 ->
                            placesViewModel.changeFavorite(p0)
                        },
                        deletePlace = { p0 ->
                            if (!p0.favorite) {
                                placesViewModel.deletePlace(p0.uuid)
                            } else {
                                Toast.makeText(appState.context, R.string.can_not_delete_favorite_place, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceItem(
    modifier: Modifier = Modifier,
    place: PlaceWithAQIAndWeather,
    aqiType: String,
    onClick: (uuid: String) -> Unit,
    changeFavorite: (p0: Place) -> Unit,
    deletePlace: (p0: Place) -> Unit,
) {
    Panel(
        onClick = { onClick(place.place.uuid) },
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { changeFavorite(place.place) },
                modifier = Modifier
                    .width(40.dp)
                    .height(IntrinsicSize.Min)
                    .aspectRatio(1f)
            ) {
                if (!place.place.favorite) {
                    Icon(
                        imageVector = Icons.Rounded.StarOutline,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            PlaceInfo(
                modifier = Modifier
                    .weight(1f),
                place = place,
                aqiType = aqiType
            )
            IconButton(
                onClick = { deletePlace(place.place) },
                modifier = Modifier
                    .width(40.dp)
                    .height(IntrinsicSize.Min)
                    .aspectRatio(1f)
            ) {
                Icon(
                    imageVector = Icons.Rounded.DeleteForever,
                    contentDescription = null,
                    tint = StarDust,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun PlaceInfo(
    modifier: Modifier = Modifier,
    place: PlaceWithAQIAndWeather,
    aqiType: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(
            text = place.place.formattedAddress,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.small,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentWeather = place.weathers.toWeatherInfo().currentWeather
                currentWeather?.weathercode?.let {
                    Icon(
                        painter = painterResource(id = WeatherType.fromWMO(it).iconRes),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
                Text(
                    text = stringResource(
                        id = R.string.temperature_with_unit,
                        place.weathers
                            .toWeatherInfo().currentWeather?.temperature2m?.toString() ?: ""
                    ),
                    style = MaterialTheme.typography.body1
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Caption(label = stringResource(id = if (aqiType == AqiTypes.UsAQI.type) R.string.us_aqi else R.string.eu_aqi))
                Text(
                    text = if (aqiType == AqiTypes.UsAQI.type) {
                        place.aqiQualities.toAirQualityInfo().currentAirQuality?.usAqi?.toString()
                            ?: ""
                    } else {
                        place.aqiQualities.toAirQualityInfo().currentAirQuality?.europeanAqi?.toString()
                            ?: ""
                    },
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}