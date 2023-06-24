package ua.airweath.usecase.locationbyip

import ua.airweath.database.place.PlaceRepository
import ua.airweath.ktor.NetworkResponse
import ua.airweath.ktor.ServiceApi
import ua.airweath.ktor.data.geocoding.Results
import javax.inject.Inject
import javax.inject.Singleton

interface ILocationByIPUseCase {
    suspend fun getLocationByIp(): List<Results>?
}

@Singleton
class LocationByIPUseCase @Inject constructor(
    private val serviceApi: ServiceApi,
): ILocationByIPUseCase {
    override suspend fun getLocationByIp(): List<Results>? {
        serviceApi.getGeocodingByLatLngDefault().also { response ->
            return if (response is NetworkResponse.Success)
                response.data.results
            else null
        }
    }
}