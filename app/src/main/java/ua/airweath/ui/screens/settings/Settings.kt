package ua.airweath.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.airweath.R
import ua.airweath.enums.AqiTypes
import ua.airweath.ui.AppState
import ua.airweath.ui.components.AppDropDown
import ua.airweath.ui.components.TopAppBar
import ua.airweath.ui.theme.spacing
import ua.airweath.utils.findActivity
import java.util.Locale

@Composable
fun Settings(
    appState: AppState,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {

    val isCurrentLocationUsed by settingsViewModel.isCurrentLocationUsed.collectAsStateWithLifecycle(
        initialValue = false
    )
    val aqiType by settingsViewModel.aqiType.collectAsStateWithLifecycle(initialValue = "")
    val updateInterval by settingsViewModel.updateInterval.collectAsStateWithLifecycle(initialValue = 0)
    val notifyEnabled by settingsViewModel.notifyEnabled.collectAsStateWithLifecycle(initialValue = false)
    val notifyInterval by settingsViewModel.notifyInterval.collectAsStateWithLifecycle(initialValue = 0)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = stringResource(id = R.string.settings)) {
                appState.navController.navigateUp()
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
            ) {
                val listLang = listOf(Locale.US, Locale("uk", "UA"))
                SettingWithDropDown(
                    text = stringResource(id = R.string.language),
                    value = appState.context.resources.configuration.locales[0].displayName,
                    options = listLang.map { it.displayName },
                    onClick = { id, option ->
                        settingsViewModel.setLocale(listLang[id].language)
                        appState.context.findActivity().recreate()
                    }
                )
                val listAQI = listOf(AqiTypes.UsAQI, AqiTypes.EuAQI)
                SettingWithDropDown(
                    text = stringResource(id = R.string.current_aqi_scheme),
                    value = aqiType,
                    options = listAQI.map { it.type.replace("_", " ") },
                    onClick = { id, option ->
                        settingsViewModel.setAQIType(listAQI[id].type)
                    }
                )
                SettingWithCheckBox(
                    text = stringResource(id = R.string.use_current_location),
                    checked = isCurrentLocationUsed,
                    onCheckedChange = {
                        settingsViewModel.changeCurrentLocationUsed(it, appState.context)
                    }
                )
                val listUpdateInterval = listOf(1, 2, 3, 4, 5)
                SettingWithDropDown(
                    text = stringResource(id = R.string.aqi_data_update_interval),
                    value = updateInterval.toString(),
                    options = listUpdateInterval.map { it.toString() },
                    onClick = { id, option ->
                        settingsViewModel.setUpdateTime(listUpdateInterval[id])
                    }
                )
                SettingWithCheckBox(
                    text = stringResource(id = R.string.allow_aqi_alerts),
                    checked = notifyEnabled,
                    onCheckedChange = {
                        settingsViewModel.setNotifyEnabled(it)
                    }
                )
                val listNotifyInterval = listOf(1, 2, 3, 5, 7)
                SettingWithDropDown(
                    text = stringResource(id = R.string.notification_interval),
                    value = notifyInterval.toString(),
                    options = listNotifyInterval.map { it.toString() },
                    onClick = { id, option ->
                        settingsViewModel.setNotifyTime(listNotifyInterval[id])
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingWithDropDown(
    text: String,
    value: String,
    enabled: Boolean = true,
    options: List<String>,
    onClick: (id: Int, option: String) -> Unit,
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(1f),
            style = MaterialTheme.typography.subtitle1
        )
        AppDropDown(
            modifier = Modifier,
            options = options,
            expanded = expanded.value,
            onExpandedChange = { if (enabled) expanded.value = !expanded.value },
            onDismissRequest = { expanded.value = false },
            onClick = { id, option ->
                onClick(id, option)
                expanded.value = false
            },
            value = value,
            onValueChange = {},
            readOnly = true,
        )
    }
}

@Composable
private fun SettingWithCheckBox(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier
                .weight(1f),
            style = MaterialTheme.typography.subtitle1
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .size(40.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.primary
            )
        )
    }
}