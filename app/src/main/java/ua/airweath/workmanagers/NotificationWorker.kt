package ua.airweath.workmanagers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import ua.airweath.R
import ua.airweath.data.mappers.toAirQualityInfo
import ua.airweath.database.airquality.AirQuality
import ua.airweath.database.place.PlaceRepository
import ua.airweath.datastore.ProtoApi
import ua.airweath.enums.AqiTypes
import ua.airweath.enums.EUAqiRanges
import ua.airweath.enums.USAqiRanges
import ua.airweath.notification.NotificationChannels
import ua.airweath.notification.NotificationConstants
import ua.airweath.notification.setNotification
import ua.airweath.utils.location.LocationManager
import java.time.LocalDateTime

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val placeRepository: PlaceRepository,
    private val locationManager: LocationManager,
    private val protoApi: ProtoApi,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Timber.d("Start worker")
        val aqiType = protoApi.aqiType.first()
        Timber.d("aqiType $aqiType")
        val isCurrentLocationUsed = protoApi.isCurrentLocationUsed.first()
        Timber.d("isCurrentLocationUsed $isCurrentLocationUsed")

        if (isCurrentLocationUsed) {
            placeRepository.getCurrentPlacesInfo().firstOrNull()
                ?.aqiQualities?.toAirQualityInfo()?.currentAirQuality?.let { airQuality ->
                    Timber.d("Current location airQuality $airQuality")
                    setNotification(aqiType, airQuality)
                }
        } else {
            placeRepository.getFavoritePlace().firstOrNull()?.let { place ->
                Timber.d("Place info $place")
                place.aqiQualities.toAirQualityInfo().currentAirQuality?.let { airQuality ->
                    Timber.d("airQuality $airQuality")
                    setNotification(aqiType, airQuality)
                }
            }
        }

        return Result.success()
    }

    private fun setNotification(aqiType: String, airQuality: AirQuality) {
        val now = LocalDateTime.now()
        val silent = now.hour !in 8..20
        when (aqiType) {
            AqiTypes.UsAQI.type -> {
                airQuality.usAqi?.let { aqi ->
                    when (aqi) {
                        in USAqiRanges.Good.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_us_good),
                                message = null,
                                NotificationCompat.PRIORITY_LOW,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_good))
                            )
                        }

                        in USAqiRanges.Moderate.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_us_moderate),
                                message = null,
                                NotificationCompat.PRIORITY_DEFAULT,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_moderate))
                            )
                        }

                        in USAqiRanges.UnhealthyForSensitiveGroups.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_us_unhealthy_for_sensitive_groups),
                                message = null,
                                NotificationCompat.PRIORITY_HIGH,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_unhealthy_for_sensitive_groups))
                            )
                        }

                        in USAqiRanges.Unhealthy.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_us_unhealthy),
                                message = null,
                                NotificationCompat.PRIORITY_HIGH,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_unhealthy))
                            )
                        }

                        in USAqiRanges.VeryUnhealthy.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_us_very_unhealthy),
                                message = null,
                                NotificationCompat.PRIORITY_MAX,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_very_unhealthy))
                            )
                        }

                        in USAqiRanges.Hazardous.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_us_hazardous),
                                message = null,
                                NotificationCompat.PRIORITY_MAX,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_hazardous))
                            )
                        }
                    }
                }
            }
            AqiTypes.EuAQI.type -> {
                airQuality.europeanAqi?.let { aqi ->
                    when (aqi) {
                        in EUAqiRanges.Good.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_eu_good),
                                message = null,
                                NotificationCompat.PRIORITY_LOW,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_good))
                            )
                        }

                        in EUAqiRanges.Fair.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_eu_fair),
                                message = null,
                                NotificationCompat.PRIORITY_DEFAULT,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_moderate))
                            )
                        }

                        in EUAqiRanges.Moderate.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_eu_moderate),
                                message = null,
                                NotificationCompat.PRIORITY_DEFAULT,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_unhealthy_for_sensitive_groups))
                            )
                        }

                        in EUAqiRanges.Poor.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_eu_poor),
                                message = null,
                                NotificationCompat.PRIORITY_HIGH,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_unhealthy))
                            )
                        }

                        in EUAqiRanges.VeryPoor.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_eu_very_poor),
                                message = null,
                                NotificationCompat.PRIORITY_MAX,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_very_unhealthy))
                            )
                        }

                        in EUAqiRanges.ExtremelyPoor.range -> {
                            setNotification(
                                context = context,
                                title = context.resources.getString(R.string.aqi_eu_extremely_poor),
                                message = null,
                                NotificationCompat.PRIORITY_MAX,
                                channelId = NotificationChannels.MAIN_CHANNEL,
                                tag = NotificationConstants.AQI_UPDATE,
                                idNotification = System.currentTimeMillis().toInt(),
                                silent = silent,
                                style = NotificationCompat.BigTextStyle()
                                    .bigText(context.resources.getString(R.string.aqi_hazardous))
                            )
                        }
                    }
                }
            }
        }
        airQuality.ammonia?.let { ammonia ->
            if (ammonia >= 14.0) {
                setNotification(
                    context = context,
                    title = context.resources.getString(R.string.exceeding_ammonia_standards),
                    message = null,
                    NotificationCompat.PRIORITY_MAX,
                    channelId = NotificationChannels.MAIN_CHANNEL,
                    tag = NotificationConstants.AQI_UPDATE,
                    idNotification = System.currentTimeMillis().toInt(),
                    silent = false,
                    style = NotificationCompat.BigTextStyle()
                        .bigText(context.resources.getString(R.string.warrning_amonnia))
                )
            }
        }
    }

}