package ua.airweath.datastore.data

data class UserData(
    val userId: String,
    val name: String,
    val pictureUrl: String,
    val mail: String? = "",
    val isGoogleAuth: Boolean,
)
