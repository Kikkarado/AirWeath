package ua.airweath

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import ua.airweath.database.place.PlaceRepository
import ua.airweath.notification.NotificationChannels.MAIN_CHANNEL
import javax.inject.Inject

@HiltAndroidApp
class Application: Application(), Configuration.Provider {

    @Inject lateinit var placeRepository: PlaceRepository

    @Inject lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        CoroutineScope(Dispatchers.Main).launch {
            Timber.d(placeRepository.getPlaces().first().toString())
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                MAIN_CHANNEL,
                "General Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "This is default channel used for all other notifications"
                enableVibration(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
                setShowBadge(true)
            }

            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

}