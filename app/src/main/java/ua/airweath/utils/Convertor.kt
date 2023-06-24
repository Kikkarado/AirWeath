package ua.airweath.utils

import android.os.Build
import ua.airweath.enums.EUAqiRanges
import ua.airweath.enums.EUAqiRangesNO2
import ua.airweath.enums.EUAqiRangesO3
import ua.airweath.enums.EUAqiRangesPM10
import ua.airweath.enums.EUAqiRangesPM25
import ua.airweath.enums.EUAqiRangesSO2
import ua.airweath.enums.USAqiRanges
import ua.airweath.enums.USAqiRangesCO
import ua.airweath.enums.USAqiRangesNO2
import ua.airweath.enums.USAqiRangesO3
import ua.airweath.enums.USAqiRangesPM10
import ua.airweath.enums.USAqiRangesPM25
import ua.airweath.enums.USAqiRangesSO2
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String.getIntString(): String? {
    if (this.toIntOrNull() != null) {
        if (this.length > 1 && this.first() == '0' && this[1] == '0')
            return this.substring(0 until this.length - 1)
        if (this.length > 1 && this.first() == '0')
            return this.substring(1 until this.length)
        return this
    }
    return null
}

fun String.getDoubleString(): String? {
    if (this.isBlank()) return ""
    if (this.toDoubleOrNull() != null) {
        if (this.length > 1 && this.first() == '0' && this[1] == '0')
            return this.substring(0 until this.length - 1)
        if (this.length > 1 && this.first() == '0' && this[1] != '.')
            return this.substring(1 until this.length)
        return this
    }
    return null
}

fun String.toLocalDateTime(): LocalDateTime? {
    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
    } catch (_: Exception) {
        null
    }
}

fun LocalDateTime.toDate(pattern: String = "dd.MM.yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(this)
}

fun LocalDateTime.toTime(pattern: String = "HH:mm"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(this)
}

const val molarVolume = 24.45

fun Float.toPpbO3(): Float {
    val molecularWeight = 48
    val ppb = molarVolume * this / molecularWeight
    return ppb.toFloat()
}

fun Float.toPpmCO(): Float {
    val molecularWeight = 28.01
    val ppb = molarVolume * this / molecularWeight
    return ppb.toFloat() / 1000
}

fun Float.toPpbSO2(): Float {
    val molecularWeight = 64.06
    val ppb = molarVolume * this / molecularWeight
    return ppb.toFloat()
}

fun Float.toPpbNO2(): Float {
    val molecularWeight = 46.01
    val ppb = molarVolume * this / molecularWeight
    return ppb.toFloat()
}

fun Float.calculateUSAQIForPM25(): Int {
    return when (this.toInt()) {
        in USAqiRangesPM25.Good.range -> scaleValue(this, USAqiRangesPM25.Good.range, USAqiRanges.Good.range)
        in USAqiRangesPM25.Moderate.range -> scaleValue(this, USAqiRangesPM25.Moderate.range, USAqiRanges.Moderate.range)
        in USAqiRangesPM25.UnhealthyForSensitiveGroups.range -> scaleValue(this, USAqiRangesPM25.UnhealthyForSensitiveGroups.range, USAqiRanges.UnhealthyForSensitiveGroups.range)
        in USAqiRangesPM25.Unhealthy.range -> scaleValue(this, USAqiRangesPM25.Unhealthy.range, USAqiRanges.Unhealthy.range)
        in USAqiRangesPM25.VeryUnhealthy.range -> scaleValue(this, USAqiRangesPM25.VeryUnhealthy.range, USAqiRanges.VeryUnhealthy.range)
        else -> scaleValue(this, USAqiRangesPM25.Hazardous.range, USAqiRanges.Hazardous.range)
    }
}

fun Float.calculateUSAQIForPM10(): Int {
    return when (this.toInt()) {
        in USAqiRangesPM10.Good.range -> scaleValue(this, USAqiRangesPM10.Good.range, USAqiRanges.Good.range)
        in USAqiRangesPM10.Moderate.range -> scaleValue(this, USAqiRangesPM10.Moderate.range, USAqiRanges.Moderate.range)
        in USAqiRangesPM10.UnhealthyForSensitiveGroups.range -> scaleValue(this, USAqiRangesPM10.UnhealthyForSensitiveGroups.range, USAqiRanges.UnhealthyForSensitiveGroups.range)
        in USAqiRangesPM10.Unhealthy.range -> scaleValue(this, USAqiRangesPM10.Unhealthy.range, USAqiRanges.Unhealthy.range)
        in USAqiRangesPM10.VeryUnhealthy.range -> scaleValue(this, USAqiRangesPM10.VeryUnhealthy.range, USAqiRanges.VeryUnhealthy.range)
        else -> scaleValue(this, USAqiRangesPM10.Hazardous.range, USAqiRanges.Hazardous.range)
    }
}

fun Float.calculateUSAQIForO3(): Int {
    return when (this.toInt()) {
        in USAqiRangesO3.Good.range -> scaleValue(this, USAqiRangesO3.Good.range, USAqiRanges.Good.range)
        in USAqiRangesO3.Moderate.range -> scaleValue(this, USAqiRangesO3.Moderate.range, USAqiRanges.Moderate.range)
        in USAqiRangesO3.UnhealthyForSensitiveGroups.range -> scaleValue(this, USAqiRangesO3.UnhealthyForSensitiveGroups.range, USAqiRanges.UnhealthyForSensitiveGroups.range)
        in USAqiRangesO3.Unhealthy.range -> scaleValue(this, USAqiRangesO3.Unhealthy.range, USAqiRanges.Unhealthy.range)
        in USAqiRangesO3.VeryUnhealthy.range -> scaleValue(this, USAqiRangesO3.VeryUnhealthy.range, USAqiRanges.VeryUnhealthy.range)
        else -> scaleValue(this, USAqiRangesO3.Hazardous.range, USAqiRanges.Hazardous.range)
    }
}

fun Float.calculateUSAQIForCO(): Int {
    return when (this.toInt()) {
        in USAqiRangesCO.Good.range -> scaleValue(this, USAqiRangesCO.Good.range, USAqiRanges.Good.range)
        in USAqiRangesCO.Moderate.range -> scaleValue(this, USAqiRangesCO.Moderate.range, USAqiRanges.Moderate.range)
        in USAqiRangesCO.UnhealthyForSensitiveGroups.range -> scaleValue(this, USAqiRangesCO.UnhealthyForSensitiveGroups.range, USAqiRanges.UnhealthyForSensitiveGroups.range)
        in USAqiRangesCO.Unhealthy.range -> scaleValue(this, USAqiRangesCO.Unhealthy.range, USAqiRanges.Unhealthy.range)
        in USAqiRangesCO.VeryUnhealthy.range -> scaleValue(this, USAqiRangesCO.VeryUnhealthy.range, USAqiRanges.VeryUnhealthy.range)
        else -> scaleValue(this, USAqiRangesCO.Hazardous.range, USAqiRanges.Hazardous.range)
    }
}

fun Float.calculateUSAQIForSO2(): Int {
    return when (this.toInt()) {
        in USAqiRangesSO2.Good.range -> scaleValue(this, USAqiRangesSO2.Good.range, USAqiRanges.Good.range)
        in USAqiRangesSO2.Moderate.range -> scaleValue(this, USAqiRangesSO2.Moderate.range, USAqiRanges.Moderate.range)
        in USAqiRangesSO2.UnhealthyForSensitiveGroups.range -> scaleValue(this, USAqiRangesSO2.UnhealthyForSensitiveGroups.range, USAqiRanges.UnhealthyForSensitiveGroups.range)
        in USAqiRangesSO2.Unhealthy.range -> scaleValue(this, USAqiRangesSO2.Unhealthy.range, USAqiRanges.Unhealthy.range)
        in USAqiRangesSO2.VeryUnhealthy.range -> scaleValue(this, USAqiRangesSO2.VeryUnhealthy.range, USAqiRanges.VeryUnhealthy.range)
        else -> scaleValue(this, USAqiRangesSO2.Hazardous.range, USAqiRanges.Hazardous.range)
    }
}

fun Float.calculateUSAQIForNO2(): Int {
    return when (this.toInt()) {
        in USAqiRangesNO2.Good.range -> scaleValue(this, USAqiRangesNO2.Good.range, USAqiRanges.Good.range)
        in USAqiRangesNO2.Moderate.range -> scaleValue(this, USAqiRangesNO2.Moderate.range, USAqiRanges.Moderate.range)
        in USAqiRangesNO2.UnhealthyForSensitiveGroups.range -> scaleValue(this, USAqiRangesNO2.UnhealthyForSensitiveGroups.range, USAqiRanges.UnhealthyForSensitiveGroups.range)
        in USAqiRangesNO2.Unhealthy.range -> scaleValue(this, USAqiRangesNO2.Unhealthy.range, USAqiRanges.Unhealthy.range)
        in USAqiRangesNO2.VeryUnhealthy.range -> scaleValue(this, USAqiRangesNO2.VeryUnhealthy.range, USAqiRanges.VeryUnhealthy.range)
        else -> scaleValue(this, USAqiRangesNO2.Hazardous.range, USAqiRanges.Hazardous.range)
    }
}

fun Float.calculateEUAQIForPM25(): Int {
    return when (this.toInt()) {
        in EUAqiRangesPM25.Good.range -> scaleValue(this, EUAqiRangesPM25.Good.range, EUAqiRanges.Good.range)
        in EUAqiRangesPM25.Fair.range -> scaleValue(this, EUAqiRangesPM25.Fair.range, EUAqiRanges.Fair.range)
        in EUAqiRangesPM25.Moderate.range -> scaleValue(this, EUAqiRangesPM25.Moderate.range, EUAqiRanges.Moderate.range)
        in EUAqiRangesPM25.Poor.range -> scaleValue(this, EUAqiRangesPM25.Poor.range, EUAqiRanges.Poor.range)
        in EUAqiRangesPM25.VeryPoor.range -> scaleValue(this, EUAqiRangesPM25.VeryPoor.range, EUAqiRanges.VeryPoor.range)
        else -> scaleValue(this, EUAqiRangesPM25.ExtremelyPoor.range, EUAqiRanges.ExtremelyPoor.range)
    }
}

fun Float.calculateEUAQIForPM10(): Int {
    return when (this.toInt()) {
        in EUAqiRangesPM10.Good.range -> scaleValue(this, EUAqiRangesPM10.Good.range, EUAqiRanges.Good.range)
        in EUAqiRangesPM10.Fair.range -> scaleValue(this, EUAqiRangesPM10.Fair.range, EUAqiRanges.Fair.range)
        in EUAqiRangesPM10.Moderate.range -> scaleValue(this, EUAqiRangesPM10.Moderate.range, EUAqiRanges.Moderate.range)
        in EUAqiRangesPM10.Poor.range -> scaleValue(this, EUAqiRangesPM10.Poor.range, EUAqiRanges.Poor.range)
        in EUAqiRangesPM10.VeryPoor.range -> scaleValue(this, EUAqiRangesPM10.VeryPoor.range, EUAqiRanges.VeryPoor.range)
        else -> scaleValue(this, EUAqiRangesPM10.ExtremelyPoor.range, EUAqiRanges.ExtremelyPoor.range)
    }
}

fun Float.calculateEUAQIForNO2(): Int {
    return when (this.toInt()) {
        in EUAqiRangesNO2.Good.range -> scaleValue(this, EUAqiRangesNO2.Good.range, EUAqiRanges.Good.range)
        in EUAqiRangesNO2.Fair.range -> scaleValue(this, EUAqiRangesNO2.Fair.range, EUAqiRanges.Fair.range)
        in EUAqiRangesNO2.Moderate.range -> scaleValue(this, EUAqiRangesNO2.Moderate.range, EUAqiRanges.Moderate.range)
        in EUAqiRangesNO2.Poor.range -> scaleValue(this, EUAqiRangesNO2.Poor.range, EUAqiRanges.Poor.range)
        in EUAqiRangesNO2.VeryPoor.range -> scaleValue(this, EUAqiRangesNO2.VeryPoor.range, EUAqiRanges.VeryPoor.range)
        else -> scaleValue(this, EUAqiRangesNO2.ExtremelyPoor.range, EUAqiRanges.ExtremelyPoor.range)
    }
}

fun Float.calculateEUAQIForO3(): Int {
    return when (this.toInt()) {
        in EUAqiRangesO3.Good.range -> scaleValue(this, EUAqiRangesO3.Good.range, EUAqiRanges.Good.range)
        in EUAqiRangesO3.Fair.range -> scaleValue(this, EUAqiRangesO3.Fair.range, EUAqiRanges.Fair.range)
        in EUAqiRangesO3.Moderate.range -> scaleValue(this, EUAqiRangesO3.Moderate.range, EUAqiRanges.Moderate.range)
        in EUAqiRangesO3.Poor.range -> scaleValue(this, EUAqiRangesO3.Poor.range, EUAqiRanges.Poor.range)
        in EUAqiRangesO3.VeryPoor.range -> scaleValue(this, EUAqiRangesO3.VeryPoor.range, EUAqiRanges.VeryPoor.range)
        else -> scaleValue(this, EUAqiRangesO3.ExtremelyPoor.range, EUAqiRanges.ExtremelyPoor.range)
    }
}

fun Float.calculateEUAQIForSO2(): Int {
    return when (this.toInt()) {
        in EUAqiRangesSO2.Good.range -> scaleValue(this, EUAqiRangesSO2.Good.range, EUAqiRanges.Good.range)
        in EUAqiRangesSO2.Fair.range -> scaleValue(this, EUAqiRangesSO2.Fair.range, EUAqiRanges.Fair.range)
        in EUAqiRangesSO2.Moderate.range -> scaleValue(this, EUAqiRangesSO2.Moderate.range, EUAqiRanges.Moderate.range)
        in EUAqiRangesSO2.Poor.range -> scaleValue(this, EUAqiRangesSO2.Poor.range, EUAqiRanges.Poor.range)
        in EUAqiRangesSO2.VeryPoor.range -> scaleValue(this, EUAqiRangesSO2.VeryPoor.range, EUAqiRanges.VeryPoor.range)
        else -> scaleValue(this, EUAqiRangesSO2.ExtremelyPoor.range, EUAqiRanges.ExtremelyPoor.range)
    }
}

private fun scaleValue(value: Float, range: IntRange, rangeAqi: IntRange): Int {
    val scaledValue = (((rangeAqi.last - rangeAqi.first).toFloat() / (range.last - range.first)) * (value - range.first)).toInt()
    return scaledValue + rangeAqi.first
}