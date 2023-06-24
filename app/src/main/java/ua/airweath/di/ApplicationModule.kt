package ua.airweath.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import ua.airweath.datastore.ProtoApi
import ua.airweath.datastore.ProtoRepository
import ua.airweath.datastore.SettingsProto
import ua.airweath.datastore.SettingsProtoSerializer
import ua.airweath.ktor.ServiceApi
import ua.airweath.ktor.ServiceApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideKtorClient(
        @ApplicationContext context: Context,
    ): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60000
            }
        }
    }

    @Singleton
    @Provides
    fun provideServiceApi(impl: ServiceApiImpl): ServiceApi {
        return impl
    }

    @Singleton
    @Provides
    fun provideProtoDatastore(@ApplicationContext context: Context): DataStore<SettingsProto> {
        return DataStoreFactory.create(
            serializer = SettingsProtoSerializer,
            produceFile = { context.dataStoreFile("SettingsFile") },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideProtoApi(protoRepository: ProtoRepository): ProtoApi {
        return protoRepository
    }

}