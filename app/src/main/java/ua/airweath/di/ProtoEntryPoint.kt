package ua.airweath.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.airweath.database.airquality.AirQualityRepository
import ua.airweath.datastore.ProtoApi

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ProtoEntryPoint {
    val protoApi: ProtoApi
}
