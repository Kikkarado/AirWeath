package ua.airweath.ui.screens.statistics

import android.graphics.Typeface
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import timber.log.Timber
import ua.airweath.R
import ua.airweath.enums.AQIPeriods
import ua.airweath.enums.AqiTypes
import ua.airweath.ui.AppState
import ua.airweath.ui.components.AppDropDown
import ua.airweath.ui.components.InformationDialog
import ua.airweath.ui.components.Panel
import ua.airweath.ui.components.TopAppBar
import ua.airweath.ui.theme.customShapes
import ua.airweath.ui.theme.spacing
import ua.airweath.utils.AQIRecommendation
import ua.airweath.utils.AqiColor
import ua.airweath.utils.rememberChartStyle
import ua.airweath.utils.rememberMarker
import ua.airweath.utils.rememberThresholdLine

@Composable
fun Statistics(
    appState: AppState,
    placeUUID: String,
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        statisticsViewModel.getAqiType()
        statisticsViewModel.getPlace(placeUUID)
        statisticsViewModel.updateModelAndData(
            context = appState.context,
            placeUUID = placeUUID,
            period = AQIPeriods.Current
        )
    }


    //val place by statisticsViewModel.place
    val aqiType by statisticsViewModel.aqiType
    
    /*val currentAqiInfo = remember(place) {
        place?.aqiQualities?.toAirQualityInfo()
    }*/
    //val averageAqi = currentAqiInfo?.airQualityMap?.get(0)?.averageAqiInt(aqiType)

    val periods = AQIPeriods.listOfTypes()
    val dropDownValue = remember {
        mutableStateOf(appState.context.getString(AQIPeriods.Current.string))
    }

    val chartModel by statisticsViewModel.chartModel
    val formatter by statisticsViewModel.formatter
    val airQualityInfo by statisticsViewModel.airQualityInfo
    val averageAqi by statisticsViewModel.averageAqi
    val xAxisTitle by statisticsViewModel.xAxisTitle
    val maxAqi by statisticsViewModel.maxAqi
    val minAqi by statisticsViewModel.minAqi

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = stringResource(id = R.string.statistics)) {
                appState.navController.navigateUp()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.choose_a_period),
                            style = MaterialTheme.typography.subtitle1
                        )
                        val expanded = remember {
                            mutableStateOf(false)
                        }
                        AppDropDown(
                            options = periods.map {
                                if (it.pluralValue > 0) {
                                    pluralStringResource(
                                        id = it.plurals,
                                        it.pluralValue,
                                        it.pluralValue
                                    )
                                } else {
                                    stringResource(id = it.string)
                                }
                            },
                            expanded = expanded.value,
                            onExpandedChange = {
                                expanded.value = !expanded.value
                            },
                            onDismissRequest = { expanded.value = false },
                            onClick = { id, _ ->
                                periods[id].run {
                                    if (pluralValue > 0) {
                                        dropDownValue.value = appState.context.resources.getQuantityString(
                                            plurals,
                                            pluralValue,
                                            pluralValue
                                        )
                                    } else {
                                        dropDownValue.value = appState.context.getString(string)
                                    }
                                }
                                statisticsViewModel.updateModelAndData(
                                    context = appState.context,
                                    placeUUID = placeUUID,
                                    period = periods[id]
                                )
                                expanded.value = false
                            },
                            value = dropDownValue.value,
                            onValueChange = {},
                            elevation = 6.dp,
                            readOnly = true
                        )
                    }
                    var marker1 = rememberMarker()
                    val thresholdLine = rememberThresholdLine(averageAqi)
                    val position = remember {
                        mutableStateOf<Float?>(null)
                    }
                    val markerValue = remember(chartModel) {
                        mutableStateOf(averageAqi)
                    }
                    val color by remember(markerValue.value) {
                        mutableStateOf(
                            markerValue.value.let { value ->
                                if (aqiType == AqiTypes.UsAQI.type) {
                                    AqiColor.usAqiColor(value.toInt())
                                } else {
                                    AqiColor.euAqiColor(value.toInt())
                                }
                            }
                        )
                    }
                    chartModel?.let { model ->
                            ProvideChartStyle(rememberChartStyle(listOf(color))) {
                                Chart(
                                    chart = lineChart(
                                        persistentMarkers = position.value.let { position ->
                                            Timber.d("Position $position")
                                            if (position == null) {
                                                null
                                            } else {
                                                remember(marker1, position) {
                                                    mapOf(
                                                        position to marker1
                                                    )
                                                }
                                            }
                                        },
                                        decorations = if (thresholdLine != null) remember(
                                            thresholdLine
                                        ) {
                                            listOf(
                                                thresholdLine
                                            )
                                        } else null,
                                        spacing = MaterialTheme.spacing.larger
                                    ),
                                    model = model,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp),
                                    startAxis = startAxis(
                                        label = axisLabelComponent(color = Color.Black),
                                        title = if (aqiType == AqiTypes.UsAQI.type) stringResource(
                                            id = R.string.us_aqi
                                        )
                                        else stringResource(id = R.string.eu_aqi),
                                        titleComponent = textComponent(
                                            color = Color.Black,
                                            padding = dimensionsOf(4.dp),
                                            margins = dimensionsOf(4.dp),
                                            typeface = Typeface.MONOSPACE,
                                        ),
                                    ),
                                    bottomAxis = bottomAxis(
                                        label = axisLabelComponent(color = Color.Black),
                                        valueFormatter = formatter,
                                        title = xAxisTitle,
                                        titleComponent = textComponent(
                                            color = Color.Black,
                                            padding = dimensionsOf(end = 4.dp),
                                            margins = dimensionsOf(top = 4.dp),
                                            typeface = Typeface.MONOSPACE,
                                        ),
                                    ),
                                    marker = marker1,
                                    markerVisibilityChangeListener = object :
                                        MarkerVisibilityChangeListener {
                                        override fun onMarkerMoved(
                                            marker: Marker,
                                            markerEntryModels: List<Marker.EntryModel>,
                                        ) {
                                            super.onMarkerMoved(marker, markerEntryModels)
                                            Timber.d("Marker moved ${markerEntryModels[0].entry.component2()}")
                                        }

                                        override fun onMarkerShown(
                                            marker: Marker,
                                            markerEntryModels: List<Marker.EntryModel>,
                                        ) {
                                            super.onMarkerShown(marker, markerEntryModels)
                                            Timber.d("Marker shown ${markerEntryModels[0].entry.component2()}")
                                            position.value = markerEntryModels[0].entry.component1()
                                            markerValue.value = markerEntryModels[0].entry.component2()
                                            marker1 = marker
                                        }

                                        override fun onMarkerHidden(marker: Marker) {
                                            super.onMarkerHidden(marker)
                                            Timber.d("Marker hidden $marker")
                                        }
                                    },
                                )
                            }
                        }
                }
                InfoPanel(
                    aqiType = aqiType,
                    value = maxAqi.toInt(),
                    description = stringResource(id = R.string.max_aqi)
                )
                InfoPanel(
                    aqiType = aqiType,
                    value = averageAqi.toInt(),
                    description = stringResource(id = R.string.average_aqi)
                )
                InfoPanel(
                    aqiType = aqiType,
                    value = minAqi.toInt(),
                    description = stringResource(id = R.string.min_aqi)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    Text(
                        text = stringResource(id = R.string.recommendation),
                        modifier = Modifier,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Start
                    )
                    val recId = if (aqiType == AqiTypes.UsAQI.type) AQIRecommendation.usAqi(averageAqi.toInt())
                    else AQIRecommendation.euAqi(averageAqi.toInt())
                    Text(
                        text = stringResource(id = recId),
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoPanel(
    aqiType: String,
    value: Int,
    description: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        elevation = 6.dp,
        shape = MaterialTheme.customShapes.small
    ) {
        val color = if (aqiType == AqiTypes.UsAQI.type) AqiColor.usAqiColor(value)
        else AqiColor.euAqiColor(value)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(10.dp)
                    .background(color)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier
                        .padding(end = MaterialTheme.spacing.large)
                )
            }
        }
    }
}