package ua.airweath.ui.screens.statistics

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ua.airweath.R
import ua.airweath.data.airquality.AirQualityInfo
import ua.airweath.data.mappers.averageAqi
import ua.airweath.data.mappers.averageAqiInt
import ua.airweath.data.mappers.daysFormatter
import ua.airweath.data.mappers.getDaysModelProducer
import ua.airweath.data.mappers.getHoursModelProducer
import ua.airweath.data.mappers.hoursFormatter
import ua.airweath.data.mappers.maxAqi
import ua.airweath.data.mappers.minAqi
import ua.airweath.data.mappers.toAirQualityInfo
import ua.airweath.data.mappers.toAirQualityList
import ua.airweath.database.place.PlaceRepository
import ua.airweath.database.relations.PlaceWithAQIAndWeather
import ua.airweath.datastore.ProtoApi
import ua.airweath.enums.AQIPeriods
import ua.airweath.enums.AqiTypes
import ua.airweath.ktor.NetworkResponse
import ua.airweath.ktor.ServiceApi
import ua.airweath.utils.ConnectionState
import ua.airweath.utils.currentConnectivityState
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val serviceApi: ServiceApi,
    private val protoApi: ProtoApi,
    private val placeRepository: PlaceRepository,
) : ViewModel() {

    private val _place = mutableStateOf<PlaceWithAQIAndWeather?>(null)
    val place: State<PlaceWithAQIAndWeather?> get() = _place

    fun getPlace(placeUUID: String) {
        viewModelScope.launch {
            _place.value = placeRepository.getPlace(placeUUID).firstOrNull()
        }
    }

    private val _aqiType = mutableStateOf(AqiTypes.UsAQI.type)
    val aqiType: State<String> get() = _aqiType

    fun getAqiType() {
        viewModelScope.launch {
            protoApi.aqiType.firstOrNull()?.let { _aqiType.value = it }
        }
    }

    private val _xAxisTitle = mutableStateOf(context.getString(R.string.hours))
    val xAxisTitle: State<String> get() = _xAxisTitle

    private val _airQualityInfo = mutableStateOf<AirQualityInfo?>(null)
    val airQualityInfo: State<AirQualityInfo?> get() = _airQualityInfo

    private val _maxAqi = mutableStateOf(.0f)
    val maxAqi: State<Float> get() = _maxAqi

    private val _averageAqi = mutableStateOf(.0f)
    val averageAqi: State<Float> get() = _averageAqi

    private val _minAqi = mutableStateOf(.0f)
    val minAqi: State<Float> get() = _minAqi

    private val _chartModel = mutableStateOf<ChartEntryModel?>(null)
    val chartModel: State<ChartEntryModel?> get() = _chartModel

    private val _formatter = mutableStateOf(hoursFormatter)
    val formatter: State<AxisValueFormatter<AxisPosition.Horizontal.Bottom>> get() = _formatter

    fun updateModelAndData(
        context: Context,
        placeUUID: String,
        period: AQIPeriods,
    ) {
        viewModelScope.launch {
            val place = placeRepository.getPlace(placeUUID).firstOrNull()
            when (period) {
                is AQIPeriods.Current -> {
                    _xAxisTitle.value = context.getString(R.string.hours)
                    _airQualityInfo.value = place?.aqiQualities?.toAirQualityInfo()
                    _averageAqi.value = _airQualityInfo.value?.airQualityMap?.get(0)
                        ?.averageAqiInt(aqiType.value)?.toFloat() ?: .0f
                    _maxAqi.value = _airQualityInfo.value?.airQualityMap?.get(0)
                        ?.maxAqi(aqiType.value)?.toFloat() ?: .0f
                    _minAqi.value = _airQualityInfo.value?.airQualityMap?.get(0)
                        ?.minAqi(aqiType.value)?.toFloat() ?: .0f
                    _chartModel.value = _airQualityInfo.value?.airQualityMap?.get(0)
                        ?.getHoursModelProducer(aqiType.value)?.getModel()
                    _formatter.value = hoursFormatter
                }

                is AQIPeriods.NextDays -> {
                    _xAxisTitle.value = context.getString(R.string.days)
                    _airQualityInfo.value = place?.aqiQualities?.toAirQualityInfo()
                    _averageAqi.value = _airQualityInfo.value?.airQualityMap
                        ?.averageAqi(aqiType.value)?.toFloat() ?: .0f
                    _maxAqi.value = _airQualityInfo.value?.airQualityMap
                        ?.maxAqi(aqiType.value)?.toFloat() ?: .0f
                    _minAqi.value = _airQualityInfo.value?.airQualityMap
                        ?.minAqi(aqiType.value)?.toFloat() ?: .0f
                    _chartModel.value = _airQualityInfo.value?.airQualityMap
                        ?.getDaysModelProducer(aqiType.value)?.getModel()
                    _formatter.value = daysFormatter
                }

                else -> {
                        if (context.currentConnectivityState == ConnectionState.Unavailable) return@launch
                        val latLng = place?.place?.let { LatLng(it.latitude, it.longitude) }
                            ?: return@launch
                        serviceApi.getPastDaysAqi(latLng = latLng, pastDays = period.days)
                            .also { response ->
                                if (response !is NetworkResponse.Success) return@launch
                                _xAxisTitle.value = context.getString(R.string.days)
                                response.data.hourly?.toAirQualityList(place.place.uuid)
                                    ?.toAirQualityInfo()?.let {
                                        _airQualityInfo.value = it
                                        _averageAqi.value = _airQualityInfo.value?.airQualityMap
                                            ?.averageAqi(aqiType.value)?.toFloat() ?: .0f
                                        _maxAqi.value = _airQualityInfo.value?.airQualityMap
                                            ?.maxAqi(aqiType.value)?.toFloat() ?: .0f
                                        _minAqi.value = _airQualityInfo.value?.airQualityMap
                                            ?.minAqi(aqiType.value)?.toFloat() ?: .0f
                                        _chartModel.value = _airQualityInfo.value?.airQualityMap
                                            ?.getDaysModelProducer(aqiType.value)?.getModel()
                                        _formatter.value = daysFormatter
                                    }
                            }
                }
            }
        }
    }

}