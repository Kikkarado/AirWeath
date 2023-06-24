package ua.airweath.utils.locale

import android.content.Context
import android.content.ContextWrapper
import java.util.Locale

class ContextUtils(base: Context) : ContextWrapper(base) {
    companion object {
        fun updateLocale(context: Context?, localeToSwitchTo: Locale): ContextUtils? {
            if (context == null) return null
            val resources = context.resources
            val configuration = resources.configuration // 1
            configuration.setLocale(localeToSwitchTo)
            configuration.setLayoutDirection(localeToSwitchTo)
            val updatedContext = context.createConfigurationContext(configuration) // 5
            return ContextUtils(updatedContext)
        }
    }
}