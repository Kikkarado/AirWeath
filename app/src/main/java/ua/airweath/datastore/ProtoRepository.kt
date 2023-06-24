package ua.airweath.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import timber.log.Timber
import ua.airweath.datastore.data.UserData
import ua.airweath.enums.AqiTypes
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtoRepository @Inject constructor(
    private val dataStore: DataStore<SettingsProto>,
) : ProtoApi {

    override val userData: Flow<UserData>
        get() = dataStore.data.map { settings ->
            try {
                settings.userDara.toUserData()
            } catch (e: Exception) {
                Timber.d(e, e.message)
                throw e
            }
        }.distinctUntilChanged()

    override suspend fun setUserData(userData: UserData) {
        dataStore.updateData { settings ->
            settings.toBuilder().setUserDara(userData.toSettingsUserData()).build()
        }
    }

    override suspend fun clearUserData() {
        dataStore.updateData { settings ->
            settings.toBuilder().clearUserDara().build()
        }
    }

    override val isFirstStart: Flow<Boolean>
        get() = dataStore.data.map { settings ->
            try {
                settings.isFirstStart
            } catch (e: Exception) {
                Timber.d(e, e.message)
                throw e
            }
        }.distinctUntilChanged()

    override suspend fun setIsFirstStart(isFirstStart: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setIsFirstStart(isFirstStart).build()
        }
    }

    override suspend fun clearIsFirstStart() {
        dataStore.updateData { settings ->
            settings.toBuilder().setIsFirstStart(true).build()
        }
    }

    override val locale: Flow<String>
        get() = dataStore.data.map { settings ->
            try {
                settings.locale
            } catch (e: Exception) {
                Timber.d(e, e.message)
                throw e
            }
        }.distinctUntilChanged()

    override suspend fun setLocale(locale: String) {
        dataStore.updateData { settings ->
            settings.toBuilder().setLocale(locale).build()
        }
    }

    override suspend fun clearLocale() {
        dataStore.updateData { settings ->
            settings.toBuilder().setLocale(Locale.getDefault().language).build()
        }
    }

    override val isCurrentLocationUsed: Flow<Boolean>
        get() = dataStore.data.map { settings ->
            try {
                settings.isCurrentLocationUsed
            } catch (e: Exception) {
                Timber.d(e, e.message)
                throw e
            }
        }.distinctUntilChanged()

    override suspend fun setIsCurrentLocationUsed(isUsed: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setIsCurrentLocationUsed(isUsed).build()
        }
    }

    override suspend fun clearIsCurrentLocationUsed() {
        dataStore.updateData { settings ->
            settings.toBuilder().clearIsCurrentLocationUsed().build()
        }
    }

    override val aqiType: Flow<String>
        get() = dataStore.data.map { settings ->
            try {
                settings.aqiType
            } catch (e: Exception) {
                Timber.d(e, e.message)
                throw e
            }
        }.distinctUntilChanged()

    override suspend fun setAqiType(type: String) {
        dataStore.updateData { settings ->
            settings.toBuilder().setAqiType(type).build()
        }
    }

    override suspend fun clearAqiType() {
        dataStore.updateData { settings ->
            settings.toBuilder().setAqiType(AqiTypes.UsAQI.type).build()
        }
    }

    override val updateTime: Flow<Int>
        get() = dataStore.data.map { settings ->
            try {
                settings.updateTime
            } catch (e: Exception) {
                Timber.d(e, e.message)
                throw e
            }
        }.distinctUntilChanged()

    override suspend fun setUpdateTime(timeInHours: Int) {
        dataStore.updateData { settings ->
            settings.toBuilder().setUpdateTime(timeInHours).build()
        }
    }

    override suspend fun clearUpdateTime() {
        dataStore.updateData { settings ->
            settings.toBuilder().setUpdateTime(1).build()
        }
    }

    override val notifyTime: Flow<Int>
        get() = dataStore.data.map { settings ->
            try {
                settings.notifyTime
            } catch (e: Exception) {
                Timber.d(e, e.message)
                throw e
            }
        }.distinctUntilChanged()

    override suspend fun setNotifyTime(timeInHours: Int) {
        dataStore.updateData { settings ->
            settings.toBuilder().setNotifyTime(timeInHours).build()
        }
    }

    override suspend fun clearNotifyTime() {
        dataStore.updateData { settings ->
            settings.toBuilder().setNotifyTime(2).build()
        }
    }

    override val notifyEnabled: Flow<Boolean>
        get() = dataStore.data.map { settings ->
            try {
                settings.notifyEnabled
            } catch (e: Exception) {
                Timber.d(e, e.message)
                throw e
            }
        }.distinctUntilChanged()

    override suspend fun setNotifyEnabled(enabled: Boolean) {
        dataStore.updateData { settings ->
            settings.toBuilder().setNotifyEnabled(enabled).build()
        }
    }

    override suspend fun clearNotifyEnabled() {
        dataStore.updateData { settings ->
            settings.toBuilder().setNotifyEnabled(true).build()
        }
    }
}