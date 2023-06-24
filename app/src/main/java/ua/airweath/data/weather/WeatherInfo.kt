package ua.airweath.data.weather

import ua.airweath.database.weather.Weather

data class WeatherInfo(
    val currentWeather: Weather?,
    val weatherMap: Map<Int, List<Weather>>,
)
