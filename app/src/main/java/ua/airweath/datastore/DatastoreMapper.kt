package ua.airweath.datastore

import ua.airweath.datastore.data.UserData

internal fun SettingsProto.UserData.toUserData() =
    UserData(
        userId = this.userId,
        name = this.name,
        pictureUrl = this.pictureUrl,
        mail = if (!this.mail.isNullOrBlank()) this.mail else null,
        isGoogleAuth = this.isGoogleAuth
    )


internal fun UserData.toSettingsUserData() =
    SettingsProto.UserData.newBuilder()
        .setUserId(this.userId)
        .setName(this.name)
        .setPictureUrl(this.pictureUrl)
        .setMail(if (!this.mail.isNullOrBlank()) this.mail else "")
        .setIsGoogleAuth(this.isGoogleAuth)
        .build()
