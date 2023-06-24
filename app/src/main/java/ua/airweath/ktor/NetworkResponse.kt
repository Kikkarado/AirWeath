package ua.airweath.ktor

sealed class NetworkResponse<out T : Any> {

    /**
     * response with a 2xx status code
     */
    data class Success<out T : Any>(val data: T) : NetworkResponse<T>()

    /**
     * response with a non-2xx status code.
     */
    data class Error<out T : Any>(
        val data: T? = null,
        val message: String,
        val code: Int,
    ) : NetworkResponse<T>()

    /**
     * response when loading.
     */
    class Loading<out T : Any>(val progress: Float? = 0.0f) : NetworkResponse<T>()

}