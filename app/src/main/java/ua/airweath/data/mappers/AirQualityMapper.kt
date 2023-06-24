package ua.airweath.data.mappers

import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import timber.log.Timber
import ua.airweath.data.airquality.AirQualityInfo
import ua.airweath.database.airquality.AirQuality
import ua.airweath.enums.AqiTypes
import ua.airweath.ktor.data.currentaqi.Hourly
import ua.airweath.utils.toLocalDateTime
import java.text.DecimalFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun Hourly.toAirQualityList(placeUUID: String): List<AirQuality>? {
    return time?.mapIndexed { index, time ->
        val pm10 = pm10?.get(index)
        val pm25 = pm25?.get(index)
        val carbonMonoxide = carbonMonoxide?.get(index)
        val nitrogenDioxide = nitrogenDioxide?.get(index)
        val sulphurDioxide = sulphurDioxide?.get(index)
        val ozone = ozone?.get(index)
        val dust = dust?.get(index)
        val ammonia = ammonia?.get(index)
        val europeanAqi = europeanAqi?.get(index)
        val europeanAqiPm25 = europeanAqiPm25?.get(index)
        val europeanAqiPm10 = europeanAqiPm10?.get(index)
        val europeanAqiNo2 = europeanAqiNo2?.get(index)
        val europeanAqiO3 = europeanAqiO3?.get(index)
        val europeanAqiSo2 = europeanAqiSo2?.get(index)
        val usAqi = usAqi?.get(index)
        val usAqiPm25 = usAqiPm25?.get(index)
        val usAqiPm10 = usAqiPm10?.get(index)
        val usAqiNo2 = usAqiNo2?.get(index)
        val usAqiCo = usAqiCo?.get(index)
        val usAqiO3 = usAqiO3?.get(index)
        val usAqiSo2 = usAqiSo2?.get(index)
        AirQuality(
            time = time,
            placeUUID = placeUUID,
            pm10 = pm10,
            pm25 = pm25,
            carbonMonoxide = carbonMonoxide,
            nitrogenDioxide = nitrogenDioxide,
            sulphurDioxide = sulphurDioxide,
            ozone = ozone,
            dust = dust,
            ammonia = ammonia,
            europeanAqi = europeanAqi,
            europeanAqiPm25 = europeanAqiPm25,
            europeanAqiPm10 = europeanAqiPm10,
            europeanAqiNo2 = europeanAqiNo2,
            europeanAqiO3 = europeanAqiO3,
            europeanAqiSo2 = europeanAqiSo2,
            usAqi = usAqi,
            usAqiPm25 = usAqiPm25,
            usAqiPm10 = usAqiPm10,
            usAqiNo2 = usAqiNo2,
            usAqiCo = usAqiCo,
            usAqiO3 = usAqiO3,
            usAqiSo2 = usAqiSo2
        )
    }
}

private data class AirQualityIndexed(
    val index: Int,
    val data: AirQuality,
)

fun List<AirQuality>.toAirQualityMap(): Map<Int, List<AirQuality>> {
    return this.mapIndexed { index, airQuality ->
        AirQualityIndexed(
            index = index,
            data = airQuality
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }
}

fun List<AirQuality>.toAirQualityInfo(): AirQualityInfo {
    val airQualityMap = this.toAirQualityMap()
    val now = LocalTime.now()
    val midnight = LocalTime.MIDNIGHT.minusMinutes(30)
    val isCurrentDay = Duration.between(midnight, now).toMinutes() < 0
    val indexMap = if (isCurrentDay) 0 else 1
    val currentAirQuality = airQualityMap[indexMap]?.find {
        val hour =
            (if (now.minute < 30) now.hour else now.hour + 1).let { hour -> if (hour > 23) 0 else hour }
        it.time.toLocalDateTime()?.hour == hour
        //LocalDateTime.parse(it.time, DateTimeFormatter.ISO_DATE_TIME).hour == hour
    }
    val index = airQualityMap[indexMap]?.indexOf(currentAirQuality)
    return AirQualityInfo(
        indexCurrent = index,
        currentAirQuality = currentAirQuality,
        airQualityMap = airQualityMap
    )
}

fun List<AirQuality>.averageAqiInt(aqiType: String): Int {
    return if (aqiType == AqiTypes.UsAQI.type) {
        this.mapNotNull { it.usAqi }.average().toInt()
    } else {
        this.mapNotNull { it.europeanAqi }.average().toInt()
    }
}

fun List<AirQuality>.maxAqi(aqiType: String): Int {
    return if (aqiType == AqiTypes.UsAQI.type) {
        this.mapNotNull { it.usAqi }.max()
    } else {
        this.mapNotNull { it.europeanAqi }.max()
    }
}

fun List<AirQuality>.minAqi(aqiType: String): Int {
    return if (aqiType == AqiTypes.UsAQI.type) {
        this.mapNotNull { it.usAqi }.min()
    } else {
        this.mapNotNull { it.europeanAqi }.min()
    }
}

fun List<AirQuality>.averageAqi(aqiType: String): String {
    val df = DecimalFormat("#.##")
    return df.format(
        if (aqiType == AqiTypes.UsAQI.type) {
            this.mapNotNull { it.usAqi }.average().toFloat()
        } else {
            this.mapNotNull { it.europeanAqi }.average().toFloat()
        }
    )
}

class Entry(
    val localDateTime: LocalDateTime,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = Entry(localDateTime, x, y)
}

fun List<AirQuality>.getHoursModelProducer(aqiType: String): ChartEntryModelProducer {
    return this.mapIndexedNotNull { index, airQuality ->
        val localDateTime = airQuality.time.toLocalDateTime()
        val aqi = if (aqiType == AqiTypes.UsAQI.type) airQuality.usAqi else airQuality.europeanAqi
        if (localDateTime == null || aqi == null) {
            null
        } else {
            Entry(localDateTime, index.toFloat(), aqi.toFloat())
        }
    }.let { ChartEntryModelProducer(it) }
}

val hoursFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
    (chartValues.chartEntryModel.entries.first().getOrNull(value.toInt()) as? Entry)
        ?.localDateTime
        ?.run { "$hour" }
        .orEmpty()
}

fun Map<Int, List<AirQuality>>.getDaysModelProducer(aqiType: String): ChartEntryModelProducer {
    return this.mapNotNull { (id, airQualities) ->
        val localDateTime = airQualities[0].time.toLocalDateTime()
        val aqi = airQualities.averageAqiInt(aqiType)
        if (localDateTime == null) {
            null
        } else {
            Entry(localDateTime, id.toFloat(), aqi.toFloat())
        }
    }.let { ChartEntryModelProducer(it) }
}

val daysFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
    (chartValues.chartEntryModel.entries.first().getOrNull(value.toInt()) as? Entry)
        ?.localDateTime
        ?.run { "$dayOfMonth.$monthValue" }
        .orEmpty()
}

fun Map<Int, List<AirQuality>>.averageAqi(aqiType: String): Int {
    return this.map { it.value.averageAqiInt(aqiType) }.average().toInt()
}

fun Map<Int, List<AirQuality>>.maxAqi(aqiType: String): Int {
    return this.map { it.value.averageAqiInt(aqiType) }.max()
}

fun Map<Int, List<AirQuality>>.minAqi(aqiType: String): Int {
    return this.map { it.value.averageAqiInt(aqiType) }.min()
}