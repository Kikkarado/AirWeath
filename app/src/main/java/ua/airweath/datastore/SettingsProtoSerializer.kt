package ua.airweath.datastore

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ua.airweath.enums.AqiTypes
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

object SettingsProtoSerializer: Serializer<SettingsProto> {

    override val defaultValue: SettingsProto
        get() = SettingsProto.getDefaultInstance()
            .toBuilder()
            .setIsFirstStart(true)
            .setLocale(Locale.getDefault().language)
            .setAqiType(AqiTypes.UsAQI.type)
            .setIsCurrentLocationUsed(false)
            .setNotifyEnabled(true)
            .setNotifyTime(2)
            .setUpdateTime(1)
            .build()

    override suspend fun readFrom(input: InputStream): SettingsProto {
        return try {
            SettingsProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            exception.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SettingsProto, output: OutputStream) {
        t.writeTo(output)
    }

}