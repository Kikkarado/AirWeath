package ua.airweath.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.airweath.R
import ua.airweath.data.mappers.averageAqiInt
import ua.airweath.data.mappers.averageTmp
import ua.airweath.data.mappers.findMostFrequentElement
import ua.airweath.data.mappers.toAirQualityInfo
import ua.airweath.data.mappers.toWeatherInfo
import ua.airweath.data.weather.WeatherType
import ua.airweath.database.airquality.AirQuality
import ua.airweath.database.weather.Weather
import ua.airweath.enums.AqiTypes
import ua.airweath.enums.CurrentLocationConst.CURRENT_LOCATION
import ua.airweath.notification.NotificationChannels
import ua.airweath.notification.NotificationConstants
import ua.airweath.notification.setNotification
import ua.airweath.ui.AppState
import ua.airweath.ui.components.AppClickTextField
import ua.airweath.ui.components.AppCustomButton
import ua.airweath.ui.components.GaugeChart
import ua.airweath.ui.components.InformationDialog
import ua.airweath.ui.components.Panel
import ua.airweath.ui.navigation.NavRoutes
import ua.airweath.ui.theme.customShapes
import ua.airweath.ui.theme.spacing
import ua.airweath.utils.AQIBrush
import ua.airweath.utils.AQIRecommendation
import ua.airweath.utils.AQIShortDescription
import ua.airweath.utils.AqiColor
import ua.airweath.utils.AqiPercent
import ua.airweath.utils.TemperatureExt
import ua.airweath.utils.connectivityState
import ua.airweath.utils.location.isProviderEnabled
import ua.airweath.utils.toDate
import ua.airweath.utils.toLocalDateTime
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalLayoutApi::class)
@Composable
fun Home(
    appState: AppState,
    placeUUID: String?,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    val scope = rememberCoroutineScope()

    val location by
    homeViewModel.location.collectAsStateWithLifecycle(initialValue = null)

    val connectionState by connectivityState()

    val isCurrentLocationUsed by homeViewModel.isCurrentLocationUsed.collectAsStateWithLifecycle(
        initialValue = true
    )
    val isLocationEnabled by homeViewModel.isLocationEnabled.collectAsStateWithLifecycle(
        initialValue = appState.context.isProviderEnabled()
    )

    val aqiType by homeViewModel.aqiType.collectAsStateWithLifecycle(initialValue = AqiTypes.UsAQI.type)

    val currentPlaceUUID by homeViewModel.placeUUID

    val placeWithAQIAndWeather by homeViewModel.getPlace(placeUUID ?: currentPlaceUUID)
        .collectAsStateWithLifecycle(
            initialValue = null
        )

    val favoritePlace by homeViewModel.favoritePlace.collectAsStateWithLifecycle(initialValue = null)

    val airQualityInfo = remember(placeWithAQIAndWeather) {
        placeWithAQIAndWeather?.aqiQualities?.toAirQualityInfo()
    }

    val weatherInfo = remember(placeWithAQIAndWeather) {
        placeWithAQIAndWeather?.weathers?.toWeatherInfo()
    }

    val usAqi = remember(placeWithAQIAndWeather) {
        airQualityInfo?.currentAirQuality?.usAqi
    }

    val euAqi = remember(placeWithAQIAndWeather) {
        airQualityInfo?.currentAirQuality?.europeanAqi
    }

    val temperature = remember(placeWithAQIAndWeather) {
        weatherInfo?.currentWeather?.temperature2m
    }

    val showAboutDialog by homeViewModel.showAboutDialog
    val textAboutDialog by homeViewModel.textAboutDialog
    val showRecommendationDialog by homeViewModel.showRecommendationDialog
    val textRecommendationDialog by homeViewModel.textRecommendationDialog

    LaunchedEffect(Unit) {
        if (!placeUUID.isNullOrBlank()) {
            homeViewModel.setPlaceUUID(placeUUID)
        }
    }

    LaunchedEffect(isCurrentLocationUsed, isLocationEnabled, connectionState, favoritePlace) {
        Timber.d("Get new AQI")
        Timber.d("isCurrentLocationUsed $isCurrentLocationUsed")
        if (placeUUID.isNullOrBlank()) {
            if (isCurrentLocationUsed) {
                Timber.d("isLocationEnabled $isLocationEnabled")
                if (isLocationEnabled) {
                    homeViewModel.startLocationUpdates()
                    homeViewModel.setPlaceUUID(CURRENT_LOCATION)
                } else {
                    homeViewModel.setPlaceUUID(favoritePlace?.place?.uuid ?: "")
                }
            } else {
                homeViewModel.setPlaceUUID(favoritePlace?.place?.uuid ?: "")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppCustomButton(
                onClick = {
                    if (placeUUID.isNullOrBlank()) {
                        scope.launch {
                            appState.scaffoldState.drawerState.open()
                        }
                    } else {
                        appState.navController.navigateUp()
                    }
                },
                modifier = Modifier
                    .size(50.dp),
                elevation = 6.dp,
                shape = MaterialTheme.customShapes.small,
                background = MaterialTheme.colors.surface
            ) {
                Icon(
                    imageVector = if (placeUUID.isNullOrBlank()) Icons.Rounded.Menu else Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.small)
                        .fillMaxSize()
                )
            }
            AppClickTextField(
                value = if (currentPlaceUUID == CURRENT_LOCATION) {
                    stringResource(id = R.string.current_location)
                } else {
                    placeWithAQIAndWeather?.place?.formattedAddress
                        ?: stringResource(id = R.string.empty)
                },
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    val latLng = location?.let { LatLng(it.latitude, it.longitude) }
                        ?: placeWithAQIAndWeather?.place.let {
                            LatLng(it?.latitude ?: .0, it?.longitude ?: .0)
                        }
                    appState.navController.navigate(
                        NavRoutes.SearchPlace(
                            latitude = latLng.latitude.toString(),
                            longitude = latLng.longitude.toString()
                        ).route
                    ) {
                        launchSingleTop = true
                    }
                },
                enabledClick = true
            )
            if (placeUUID.isNullOrBlank()) {
                AppCustomButton(
                    onClick = {
                        appState.navController.navigate(NavRoutes.Places.route) {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .size(50.dp),
                    elevation = 6.dp,
                    shape = MaterialTheme.customShapes.small,
                    background = MaterialTheme.colors.surface
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PlaylistAdd,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.small)
                            .fillMaxSize()
                    )
                }
            }
        }
        Text(
            text = LocalDateTime.now().toDate(),
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ) {
            Panel(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                enabled = false
            ) {
                val tmpColor = TemperatureExt.getTmpColor(temperature)
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.small),
                        contentAlignment = Alignment.Center
                    ) {
                        GaugeChart(
                            modifier = Modifier
                                .fillMaxWidth(.8f),
                            percent = TemperatureExt.tmpPercent(temperature),
                            elementColor = tmpColor,
                            value = temperature?.toString() ?: "--",
                            subValue = stringResource(id = R.string.celsius_unit),
                            brush = TemperatureExt.getBrush()
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        weatherInfo?.currentWeather?.weathercode?.let { code ->
                            val weatherType = WeatherType.fromWMO(code)
                            Icon(
                                painter = painterResource(weatherType.iconRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp),
                                tint = Color.Unspecified
                            )
                            Text(
                                text = stringResource(id = weatherType.weatherDesc),
                                style = MaterialTheme.typography.body1,
                            )
                        }
                    }
                }
            }
            Panel(
                onClick = {
                    appState.navController.navigate(NavRoutes.Statistics(currentPlaceUUID).route) {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                val aqiColor = if (aqiType == AqiTypes.UsAQI.type) AqiColor.usAqiColor(usAqi)
                else AqiColor.euAqiColor(euAqi)
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.small),
                        contentAlignment = Alignment.Center
                    ) {
                        GaugeChart(
                            modifier = Modifier
                                .fillMaxWidth(.8f),
                            percent = if (aqiType == AqiTypes.UsAQI.type) AqiPercent.usPercent(usAqi)
                            else AqiPercent.euPercent(euAqi),
                            elementColor = aqiColor,
                            value = (if (aqiType == AqiTypes.UsAQI.type) usAqi?.toString()
                            else euAqi?.toString()) ?: "--",
                            subValue = if (aqiType == AqiTypes.UsAQI.type) stringResource(id = R.string.us_aqi)
                            else stringResource(id = R.string.eu_aqi),
                            brush = if (aqiType == AqiTypes.UsAQI.type) AQIBrush.getUSAQIBrush() else AQIBrush.getEUAQIBrush()
                        )
                    }
                    Text(
                        text = stringResource(
                            id = if (aqiType == AqiTypes.UsAQI.type) AQIShortDescription.usAqi(
                                usAqi
                            ) else AQIShortDescription.euAqi(euAqi)
                        ),
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }
        val expanded = remember {
            mutableStateOf(false)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            expanded.value = !expanded.value
                        }
                    ),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.recommendation),
                    modifier = Modifier,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Start
                )
                Icon(
                    imageVector = if (!expanded.value) Icons.Rounded.ExpandMore else Icons.Rounded.ExpandLess,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            val recId = if (aqiType == AqiTypes.UsAQI.type) AQIRecommendation.usAqi(usAqi)
            else AQIRecommendation.euAqi(euAqi)
            if (expanded.value) {
                Text(
                    text = stringResource(id = recId),
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.subtitle1,
                    textAlign = TextAlign.Justify
                )
            }
        }
        Text(
            text = stringResource(id = R.string.forecast),
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Start
        )
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            airQualityInfo?.airQualityMap?.forEach { (id, aqi) ->
                ForecastItem(
                    onClick = { _, _aqi ->
                        val recId =
                            if (aqiType == AqiTypes.UsAQI.type) AQIRecommendation.usAqi(_aqi)
                            else AQIRecommendation.euAqi(_aqi)
                        homeViewModel.openRecommendationDialog(
                            appState.context.resources.getString(
                                recId
                            )
                        )
                    },
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .fillMaxHeight(),
                    id = id,
                    aqi = aqi,
                    weather = weatherInfo?.weatherMap?.get(id) ?: emptyList(),
                    aqiType = aqiType
                )
            }
        }
        Text(
            text = stringResource(id = R.string.breathing_now),
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Start
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            BreathItem(
                onClick = {
                    homeViewModel.openAboutDialog(text = appState.context.resources.getString(R.string.about_pm10))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = MaterialTheme.spacing.small),
                item = stringResource(id = R.string.pm_10),
                subItem = stringResource(id = R.string.particulate_matter),
                value = airQualityInfo?.currentAirQuality?.pm10 ?: .0f
            )
            BreathItem(
                onClick = {
                    homeViewModel.openAboutDialog(text = appState.context.resources.getString(R.string.about_pm25))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = MaterialTheme.spacing.small),
                item = stringResource(id = R.string.pm_2_5),
                subItem = stringResource(id = R.string.particulate_matter),
                value = airQualityInfo?.currentAirQuality?.pm25 ?: .0f
            )
            BreathItem(
                onClick = {
                    homeViewModel.openAboutDialog(text = appState.context.resources.getString(R.string.about_co))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = MaterialTheme.spacing.small),
                item = stringResource(id = R.string.co),
                subItem = stringResource(id = R.string.carbon_monoxide),
                value = airQualityInfo?.currentAirQuality?.carbonMonoxide ?: .0f
            )
            BreathItem(
                onClick = {
                    homeViewModel.openAboutDialog(text = appState.context.resources.getString(R.string.about_no2))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = MaterialTheme.spacing.small),
                item = stringResource(id = R.string.no_2),
                subItem = stringResource(id = R.string.nitrogen_dioxide),
                value = airQualityInfo?.currentAirQuality?.nitrogenDioxide ?: .0f
            )
            BreathItem(
                onClick = {
                    homeViewModel.openAboutDialog(text = appState.context.resources.getString(R.string.about_so2))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = MaterialTheme.spacing.small),
                item = stringResource(id = R.string.so_2),
                subItem = stringResource(id = R.string.sulphur_dioxide),
                value = airQualityInfo?.currentAirQuality?.sulphurDioxide ?: .0f
            )
            BreathItem(
                onClick = {
                    homeViewModel.openAboutDialog(text = appState.context.resources.getString(R.string.about_o3))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = MaterialTheme.spacing.small),
                item = stringResource(id = R.string.o_3),
                subItem = stringResource(id = R.string.ozone),
                value = airQualityInfo?.currentAirQuality?.ozone ?: .0f
            )
            BreathItem(
                onClick = {
                    homeViewModel.openAboutDialog(text = appState.context.resources.getString(R.string.about_dust))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = MaterialTheme.spacing.small),
                item = stringResource(id = R.string.dust),
                subItem = null,
                value = airQualityInfo?.currentAirQuality?.dust ?: .0f
            )
            BreathItem(
                onClick = {
                    homeViewModel.openAboutDialog(text = appState.context.resources.getString(R.string.about_nh3))
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = MaterialTheme.spacing.small),
                item = stringResource(id = R.string.nh_3),
                subItem = stringResource(id = R.string.ammonia),
                value = airQualityInfo?.currentAirQuality?.ammonia ?: .0f
            )
        }
    }

    if (showAboutDialog) {
        InformationDialog(
            onDismiss = {
                homeViewModel.closeAboutDialog()
            },
            title = stringResource(id = R.string.about_element),
            text = textAboutDialog
        )
    }

    if (showRecommendationDialog) {
        InformationDialog(
            onDismiss = {
                homeViewModel.closeRecommendationDialog()
            },
            title = stringResource(id = R.string.recommendation),
            text = textRecommendationDialog
        )
    }

    /*Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            var marker1 = rememberMarker()
            val thresholdLine = rememberThresholdLine()
            val position = remember {
                mutableStateOf(1f)
            }
            val color by remember(position.value) {
                mutableStateOf(if (position.value > 2)
                    Color.Red
                else Color.Blue)
            }
            ProvideChartStyle(rememberChartStyle(listOf(color))) {
                Chart(
                    chart = lineChart(
                        persistentMarkers = remember(marker1, position.value) { mapOf(position.value to marker1) },
                        decorations = remember(thresholdLine) { listOf(thresholdLine) }
                    ),
                    model = chartEntryModelProducer.getModel(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    startAxis = startAxis(label = axisLabelComponent(color = Color.Black)),
                    bottomAxis = bottomAxis(label = axisLabelComponent(color = Color.Black), valueFormatter = axisValueFormatter),
                    marker = marker1,
                    markerVisibilityChangeListener = object : MarkerVisibilityChangeListener {
                        override fun onMarkerMoved(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {
                            super.onMarkerMoved(marker, markerEntryModels)
                            Timber.d("Marker moved ${markerEntryModels[0].entry.component2()}")
                        }

                        override fun onMarkerShown(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {
                            super.onMarkerShown(marker, markerEntryModels)
                            Timber.d("Marker shown ${markerEntryModels[0].entry.component2()}")
                            position.value = markerEntryModels[0].entry.component1()
                            marker1 = marker
                        }
                    }
                )
            }
        }
    }*/
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ForecastItem(
    onClick: (id: Int, aqi: Int) -> Unit,
    modifier: Modifier = Modifier,
    id: Int,
    aqi: List<AirQuality>,
    weather: List<Weather>,
    aqiType: String,
) {
    val averageAqi = aqi.averageAqiInt(aqiType)
    Surface(
        onClick = { onClick(id, averageAqi) },
        modifier = modifier,
        elevation = 6.dp,
        shape = MaterialTheme.customShapes.small
    ) {
        val color = if (aqiType == AqiTypes.UsAQI.type) AqiColor.usAqiColor(averageAqi)
        else AqiColor.euAqiColor(averageAqi)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(10.dp)
                    .background(color)
            )
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.small)
            ) {
                Text(
                    text = aqi.toAirQualityInfo().currentAirQuality?.time?.toLocalDateTime()
                        ?.toDate("dd.MM") ?: "",
                    style = MaterialTheme.typography.h6
                )
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val weatherType = WeatherType.fromWMO(weather.findMostFrequentElement())
                    Icon(
                        painter = painterResource(id = weatherType.iconRes),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(28.dp)
                    )
                    Text(
                        text = stringResource(id = weatherType.weatherDesc),
                        style = MaterialTheme.typography.body1
                    )
                }
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.temperature),
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = stringResource(
                            id = R.string.temperature_with_unit,
                            weather.averageTmp()
                        ),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (aqiType == AqiTypes.UsAQI.type) stringResource(id = R.string.us_aqi)
                        else stringResource(id = R.string.eu_aqi),
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = aqi.averageAqiInt(aqiType).toString(),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BreathItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    item: String,
    subItem: String?,
    value: Float,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .height(130.dp),
        elevation = 6.dp,
        shape = MaterialTheme.customShapes.small
    ) {
        Column(
            modifier = Modifier
                .padding(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.W500)
                    )
                    Text(
                        text = stringResource(id = R.string.unit_ug_m3),
                        style = MaterialTheme.typography.caption
                    )
                }
                subItem?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.h6
            )
        }
    }
}