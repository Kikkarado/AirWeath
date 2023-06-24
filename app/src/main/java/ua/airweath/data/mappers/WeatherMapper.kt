package ua.airweath.data.mappers

import timber.log.Timber
import ua.airweath.data.weather.WeatherInfo
import ua.airweath.database.airquality.AirQuality
import ua.airweath.database.weather.Weather
import ua.airweath.enums.AqiTypes
import ua.airweath.ktor.data.currentweather.Hourly
import ua.airweath.utils.toLocalDateTime
import java.text.DecimalFormat
import java.time.Duration
import java.time.LocalTime

fun Hourly.toWeatherList(placeUUID: String): List<Weather>? {
    return time?.mapIndexed { index, time ->
        val temperature2m = temperature2m?.get(index)
        val relativehumidity2m = relativehumidity2m?.get(index)
        val apparentTemperature = apparentTemperature?.get(index)
        val precipitation = precipitation?.get(index)
        val rain = rain?.get(index)
        val weathercode = weathercode?.get(index)
        val windspeed10m = windspeed10m?.get(index)
        val windgusts10m = windgusts10m?.get(index)
        Weather(
            time = time,
            placeUUID = placeUUID,
            temperature2m = temperature2m,
            relativehumidity2m = relativehumidity2m,
            apparentTemperature = apparentTemperature,
            precipitation = precipitation,
            rain = rain,
            weathercode = weathercode,
            windspeed10m = windspeed10m,
            windgusts10m = windgusts10m
        )
    }.also {
        Timber.d("Weathers ${it?.size}")
    }
}

private data class WeatherIndexed(
    val index: Int,
    val data: Weather
)

fun List<Weather>.toWeatherMap(): Map<Int, List<Weather>> {
    return this.mapIndexed { index, weather ->
        WeatherIndexed(
            index = index,
            data = weather
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }
}

fun List<Weather>.toWeatherInfo(): WeatherInfo {
    val weatherMap = this.toWeatherMap()
    val now = LocalTime.now()
    val midnight = LocalTime.MIDNIGHT.minusMinutes(30)
    val isCurrentDay = Duration.between(midnight, now).toMinutes() < 0
    val currentWeather = weatherMap[if (isCurrentDay) 0 else 1]?.find {
        val hour = if (now.hour < 23) if(now.minute < 30) now.hour else now.hour + 1 else 0
        it.time.toLocalDateTime()?.hour == hour
    }
    return WeatherInfo(
        currentWeather = currentWeather,
        weatherMap = weatherMap
    )
}

fun List<Weather>.averageTmp(): String {
    val df = DecimalFormat("#.##")
    return df.format(this.mapNotNull { it.temperature2m }.average().toFloat())
}

fun List<Weather>.findMostFrequentElement(): Int? {
    val numbersByElement = this.groupingBy { it.weathercode }.eachCount()
    return numbersByElement.entries.maxByOrNull { it.value }?.key
}