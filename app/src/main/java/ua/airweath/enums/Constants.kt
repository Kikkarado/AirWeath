package ua.airweath.enums

import android.webkit.WebSettings.RenderPriority
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import ua.airweath.R
import kotlin.reflect.full.isSubclassOf

object CurrentLocationConst {

    const val CURRENT_LOCATION = "current_location"

}

sealed class AqiTypes(val type: String) {

    object UsAQI : AqiTypes("US_AQI")

    object EuAQI : AqiTypes("EU_AQI")

}

object WorkerKeys {
    const val ERROR = "ERROR"
}

enum class ChartValuesTypes {
    DAYS,
    HOURS
}

sealed class AQIPeriods(
    val priority: Int,
    val days: Int,
    val valueType: ChartValuesTypes,
    @StringRes val string: Int = R.string.today,
    @PluralsRes val plurals: Int = R.plurals.past_weeks,
    val pluralValue: Int = 0,
) {
    object Current : AQIPeriods(
        priority = 2,
        days = 0,
        valueType = ChartValuesTypes.HOURS,
        string = R.string.today
    )
    object NextDays : AQIPeriods(
        priority = 1,
        days = 0,
        valueType = ChartValuesTypes.DAYS,
        string = R.string.next_days
    )
    object PastWeek : AQIPeriods(
        priority = 3,
        days = 7,
        valueType = ChartValuesTypes.DAYS,
        plurals = R.plurals.past_weeks,
        pluralValue = 1
    )
    object Past2Week : AQIPeriods(
        priority = 4,
        days = 14,
        valueType = ChartValuesTypes.DAYS,
        plurals = R.plurals.past_weeks,
        pluralValue = 2
    )
    object Past3Week : AQIPeriods(
        priority = 5,
        days = 21,
        valueType = ChartValuesTypes.DAYS,
        plurals = R.plurals.past_weeks,
        pluralValue = 3
    )
    object PastMonth : AQIPeriods(
        priority = 6,
        days = 31,
        valueType = ChartValuesTypes.DAYS,
        plurals = R.plurals.past_month,
        pluralValue = 1
    )
    object Past2Month : AQIPeriods(
        priority = 7,
        days = 61,
        valueType = ChartValuesTypes.DAYS,
        plurals = R.plurals.past_month,
        pluralValue = 2
    )
    object Past3Month : AQIPeriods(
        priority = 8,
        days = 92,
        valueType = ChartValuesTypes.DAYS,
        plurals = R.plurals.past_month,
        pluralValue = 3
    )

    companion object {

        @JvmStatic
        private val listOfTypes by lazy {
            AQIPeriods::class.nestedClasses
                .filter { klass -> klass.isSubclassOf(AQIPeriods::class) }
                .map { klass -> klass.objectInstance }
                .filterIsInstance<AQIPeriods>()
                .associateBy { value -> value.priority }
                .values.sortedBy { it.priority }.toTypedArray().toList()
        }

        @JvmStatic
        fun listOfTypes() = listOfTypes
    }

}