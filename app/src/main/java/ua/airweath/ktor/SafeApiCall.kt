package ua.airweath.ktor

import android.content.Context
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ua.airweath.R
import java.io.IOException

abstract class SafeApiCall {

    suspend inline fun <reified T : Any> safeCall(
        context: Context,
        request: () -> HttpResponse,
    ): NetworkResponse<T> {
        return try {
            request().let { response ->
                if (response.status.isSuccess()) {
                    NetworkResponse.Success(response.body())
                } else {
                    NetworkResponse.Error(
                        response.body(),
                        message = response.status.description,
                        code = response.status.value
                    )
                }
            }
        } catch (e: NoTransformationFoundException) {
            NetworkResponse.Error(message = context.getString(R.string.something_went_wrong),
                code = -2)
        } catch (e: IOException) {
            NetworkResponse.Error(message = context.getString(R.string.network_connection_failure),
                code = -1)
        } catch (e: Exception) {
            NetworkResponse.Error(message = context.getString(R.string.something_went_wrong),
                code = -3)
        }
    }

}