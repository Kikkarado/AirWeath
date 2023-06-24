package ua.airweath.auth

data class SignInResult(
    val userData: UserData? = null,
    val error: String? = null,
)

data class UserData(
    val id: String? = null,
    val username: String? = null,
)
