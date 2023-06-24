package ua.airweath.auth

import android.content.Intent
import android.content.IntentSender

interface IGoogleAuth {

    suspend fun signIn():IntentSender?

    suspend fun signInWithIntent(intent: Intent): SignInResult

    suspend fun signOut()

    fun getSignedInUser(): UserData?

}