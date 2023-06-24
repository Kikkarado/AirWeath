package ua.airweath.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.airweath.database.RoomDB
import ua.airweath.database.place.PlaceDao
import ua.airweath.database.DatabaseCallback
import ua.airweath.database.airquality.AirQualityDao
import ua.airweath.database.weather.WeatherDao
import ua.airweath.usecase.update.IUpdateUseCase
import ua.airweath.usecase.locationbyip.ILocationByIPUseCase
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        iLocationByIPUseCase: Provider<ILocationByIPUseCase>,
        providerPlaceDao: Provider<PlaceDao>,
        iUpdateUseCase: Provider<IUpdateUseCase>,
    ): RoomDB {

        return Room.databaseBuilder(
            context = context,
            klass = RoomDB::class.java,
            name = "air_weath-database"
        )
            .addCallback(
                DatabaseCallback(
                    providerPlaceDao = providerPlaceDao,
                    iLocationByIPUseCase = iLocationByIPUseCase,
                    iUpdateUseCase = iUpdateUseCase
                )
            )
            .fallbackToDestructiveMigrationOnDowngrade()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePlaceDao(database: RoomDB): PlaceDao {
        return database.placeDao()
    }

    @Singleton
    @Provides
    fun provideAirQualityDao(database: RoomDB): AirQualityDao {
        return database.airQualityDao()
    }

    @Singleton
    @Provides
    fun provideWeatherDao(database: RoomDB): WeatherDao {
        return database.weatherDao()
    }

}