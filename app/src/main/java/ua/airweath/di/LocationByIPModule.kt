package ua.airweath.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.airweath.usecase.locationbyip.ILocationByIPUseCase
import ua.airweath.usecase.locationbyip.LocationByIPUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationByIPModule {

    @Provides
    @Singleton
    fun provideLocationByIPUseCase(locationByIPUseCase: LocationByIPUseCase): ILocationByIPUseCase {
        return locationByIPUseCase
    }

}