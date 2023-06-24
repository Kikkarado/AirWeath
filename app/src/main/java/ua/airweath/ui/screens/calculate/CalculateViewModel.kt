package ua.airweath.ui.screens.calculate

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.airweath.utils.calculateEUAQIForNO2
import ua.airweath.utils.calculateEUAQIForO3
import ua.airweath.utils.calculateEUAQIForPM10
import ua.airweath.utils.calculateEUAQIForPM25
import ua.airweath.utils.calculateEUAQIForSO2
import ua.airweath.utils.calculateUSAQIForCO
import ua.airweath.utils.calculateUSAQIForNO2
import ua.airweath.utils.calculateUSAQIForO3
import ua.airweath.utils.calculateUSAQIForPM10
import ua.airweath.utils.calculateUSAQIForPM25
import ua.airweath.utils.calculateUSAQIForSO2
import ua.airweath.utils.toPpbNO2
import ua.airweath.utils.toPpbO3
import ua.airweath.utils.toPpbSO2
import ua.airweath.utils.toPpmCO
import javax.inject.Inject

@HiltViewModel
class CalculateViewModel @Inject constructor() : ViewModel() {

    private val _pm25 = mutableStateOf("")
    val pm25: State<String> get() = _pm25

    fun setPM25(value: String) {
        _pm25.value = value
    }

    private val _pm10 = mutableStateOf("")
    val pm10: State<String> get() = _pm10

    fun setPM10(value: String) {
        _pm10.value = value
    }

    private val _co = mutableStateOf("")
    val co: State<String> get() = _co

    fun setCO(value: String) {
        _co.value = value
    }

    private val _no2 = mutableStateOf("")
    val no2: State<String> get() = _no2

    fun setNO2(value: String) {
        _no2.value = value
    }

    private val _so2 = mutableStateOf("")
    val so2: State<String> get() = _so2

    fun setSO2(value: String) {
        _so2.value = value
    }

    private val _o3 = mutableStateOf("")
    val o3: State<String> get() = _o3

    fun setO3(value: String) {
        _o3.value = value
    }

    private val _usAqi = mutableStateOf("")
    val usAqi: State<String> get() = _usAqi

    private val _euAqi = mutableStateOf("")
    val euAqi: State<String> get() = _euAqi

    fun calculateAqi() {
        val pm25USAqi = pm25.value.toFloatOrNull()?.calculateUSAQIForPM25() ?: 0
        val pm10USAqi = pm10.value.toFloatOrNull()?.calculateUSAQIForPM10() ?: 0
        val coUSAqi = co.value.toFloatOrNull()?.toPpmCO()?.calculateUSAQIForCO() ?: 0
        val no2USAqi = no2.value.toFloatOrNull()?.toPpbNO2()?.calculateUSAQIForNO2() ?: 0
        val so2USAqi = so2.value.toFloatOrNull()?.toPpbSO2()?.calculateUSAQIForSO2() ?: 0
        val o3USAqi = o3.value.toFloatOrNull()?.toPpbO3()?.calculateUSAQIForO3() ?: 0
        val pm25EUAqi = pm25.value.toFloatOrNull()?.calculateEUAQIForPM25() ?: 0
        val pm10EUAqi = pm25.value.toFloatOrNull()?.calculateEUAQIForPM10() ?: 0
        val no2EUAqi = pm25.value.toFloatOrNull()?.calculateEUAQIForNO2() ?: 0
        val so2EUAqi = pm25.value.toFloatOrNull()?.calculateEUAQIForSO2() ?: 0
        val o3EUAqi = pm25.value.toFloatOrNull()?.calculateEUAQIForO3() ?: 0
        _usAqi.value = maxOf(pm25USAqi, pm10USAqi, coUSAqi, no2USAqi, so2USAqi, o3USAqi).toString()
        _euAqi.value = maxOf(pm25EUAqi, pm10EUAqi, no2EUAqi, so2EUAqi, o3EUAqi).let { if (it > 100) ">100" else it.toString() }
    }

    private val _showRecommendationDialog = mutableStateOf(false)
    val showRecommendationDialog: State<Boolean> get() = _showRecommendationDialog

    private val _textRecommendationDialog = mutableStateOf("")
    val textRecommendationDialog: State<String> get() = _textRecommendationDialog

    fun openRecommendationDialog(text: String) {
        _textRecommendationDialog.value = text
        _showRecommendationDialog.value = true
    }

    fun closeRecommendationDialog(){
        _textRecommendationDialog.value = ""
        _showRecommendationDialog.value = false
    }

}