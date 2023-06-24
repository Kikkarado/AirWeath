package ua.airweath.workmanagers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import ua.airweath.database.airquality.AirQualityRepository
import ua.airweath.database.weather.WeatherRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@HiltWorker
class DeleteWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val airQualityRepository: AirQualityRepository,
    private val weatherRepository: WeatherRepository,
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Timber.d("Delete worker start")
        val now = LocalDateTime.now().minusDays(1)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val time = formatter.format(now)
        Timber.d("Time to delete: $time")
        airQualityRepository.deleteAirQualityByTime(time)
        weatherRepository.deleteWeatherByTime(time)
        return Result.success()
    }

}