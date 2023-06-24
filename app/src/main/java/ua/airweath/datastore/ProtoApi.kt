package ua.airweath.datastore

import kotlinx.coroutines.flow.Flow
import ua.airweath.datastore.data.UserData

interface ProtoApi {

    val userData: Flow<UserData>

    suspend fun setUserData(userData: UserData)

    suspend fun clearUserData()

    val isFirstStart: Flow<Boolean>

    suspend fun setIsFirstStart(isFirstStart: Boolean)

    suspend fun clearIsFirstStart()

    val locale: Flow<String>

    suspend fun setLocale(locale: String)

    suspend fun clearLocale()

    val isCurrentLocationUsed: Flow<Boolean>

    suspend fun setIsCurrentLocationUsed(isUsed: Boolean)

    suspend fun clearIsCurrentLocationUsed()

    val aqiType: Flow<String>

    suspend fun setAqiType(type: String)

    suspend fun clearAqiType()

    val updateTime: Flow<Int>

    suspend fun setUpdateTime(timeInHours: Int)

    suspend fun clearUpdateTime()

    val notifyTime: Flow<Int>

    suspend fun setNotifyTime(timeInHours: Int)

    suspend fun clearNotifyTime()

    val notifyEnabled: Flow<Boolean>

    suspend fun setNotifyEnabled(enabled: Boolean)

    suspend fun clearNotifyEnabled()

}