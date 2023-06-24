package ua.airweath.di

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import ua.airweath.workmanagers.UpdateWorker
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UpdateWorkerModule: Initializer<WorkManager> {

    @Provides
    @Singleton
    override fun create(@ApplicationContext context: Context): WorkManager {
        /*val configuration = Configuration.Builder().build()
        WorkManager.initialize(context, configuration)*/
        Timber.d("UpdateWorker initialized by Hilt this time")
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }

}