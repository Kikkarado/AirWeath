package ua.airweath.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.airweath.usecase.update.UpdateUseCase
import ua.airweath.usecase.update.IUpdateUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CurrentAQIModule {

    @Singleton
    @Provides
    fun provideCurrentAQIUseCase(updateAqiUseCase: UpdateUseCase): IUpdateUseCase {
        return updateAqiUseCase
    }

}