package ua.airweath.ui.screens.calculate

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.airweath.R
import ua.airweath.data.mappers.averageAqiInt
import ua.airweath.enums.AqiTypes
import ua.airweath.ui.AppState
import ua.airweath.ui.components.AppButton
import ua.airweath.ui.components.AppTextField
import ua.airweath.ui.components.Caption
import ua.airweath.ui.components.InformationDialog
import ua.airweath.ui.components.TopAppBar
import ua.airweath.ui.theme.customShapes
import ua.airweath.ui.theme.spacing
import ua.airweath.utils.AQIRecommendation
import ua.airweath.utils.AqiColor
import ua.airweath.utils.getDoubleString

@Composable
fun Calculate(
    appState: AppState,
    calculateViewModel: CalculateViewModel = hiltViewModel(),
) {

    val pm25 by calculateViewModel.pm25
    val pm10 by calculateViewModel.pm10
    val co by calculateViewModel.co
    val no2 by calculateViewModel.no2
    val so2 by calculateViewModel.so2
    val o3 by calculateViewModel.o3
    val usAqi by calculateViewModel.usAqi
    val euAqi by calculateViewModel.euAqi
    val showRecommendationDialog by calculateViewModel.showRecommendationDialog
    val textRecommendationDialog by calculateViewModel.textRecommendationDialog

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = stringResource(id = R.string.calculate_aqi)) {
                appState.navController.navigateUp()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .verticalScroll(rememberScrollState())
                    .padding(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
                ) {
                    EnterField(
                        title = stringResource(id = R.string.particulate_matter),
                        subTitle = stringResource(id = R.string.pm_2_5),
                        value = pm25,
                        onValueChanged = {
                            it.getDoubleString()?.let { it1 -> calculateViewModel.setPM25(it1) }
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                    EnterField(
                        title = stringResource(id = R.string.particulate_matter),
                        subTitle = stringResource(id = R.string.pm_10),
                        value = pm10,
                        onValueChanged = {
                            it.getDoubleString()?.let { it1 -> calculateViewModel.setPM10(it1) }
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
                ) {
                    EnterField(
                        title = stringResource(id = R.string.carbon_monoxide),
                        subTitle = stringResource(id = R.string.co),
                        value = co,
                        onValueChanged = {
                            it.getDoubleString()?.let { it1 -> calculateViewModel.setCO(it1) }
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                    EnterField(
                        title = stringResource(id = R.string.nitrogen_dioxide),
                        subTitle = stringResource(id = R.string.no_2),
                        value = no2,
                        onValueChanged = {
                            it.getDoubleString()?.let { it1 -> calculateViewModel.setNO2(it1) }
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
                ) {
                    EnterField(
                        title = stringResource(id = R.string.sulphur_dioxide),
                        subTitle = stringResource(id = R.string.so_2),
                        value = so2,
                        onValueChanged = {
                            it.getDoubleString()?.let { it1 -> calculateViewModel.setSO2(it1) }
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                    EnterField(
                        title = stringResource(id = R.string.ozone),
                        subTitle = stringResource(id = R.string.o_3),
                        value = o3,
                        onValueChanged = {
                            it.getDoubleString()?.let { it1 -> calculateViewModel.setO3(it1) }
                        },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
                ) {
                    AnswerField(
                        onClick = {
                            val recId = AQIRecommendation.usAqi(it)
                            calculateViewModel.openRecommendationDialog(
                                appState.context.resources.getString(
                                    recId
                                )
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Max),
                        title = stringResource(id = R.string.us_aqi),
                        value = usAqi.toIntOrNull() ?: 0,
                        aqiType = AqiTypes.UsAQI.type
                    )
                    AnswerField(
                        onClick = {
                            val recId = AQIRecommendation.euAqi(it)
                            calculateViewModel.openRecommendationDialog(
                                appState.context.resources.getString(
                                    recId
                                )
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(IntrinsicSize.Max),
                        title = stringResource(id = R.string.eu_aqi),
                        value = euAqi.toIntOrNull() ?: 0,
                        aqiType = AqiTypes.EuAQI.type
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                AppButton(
                    onClick = {
                        calculateViewModel.calculateAqi()
                    },
                    text = stringResource(id = R.string.calculate)
                )
            }
        }
    }

    if (showRecommendationDialog) {
        InformationDialog(
            onDismiss = {
                calculateViewModel.closeRecommendationDialog()
            },
            title = stringResource(id = R.string.recommendation),
            text = textRecommendationDialog
        )
    }
}

@Composable
private fun TitleField(
    title: String,
    subTitle: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1
        )
        Caption(label = subTitle)
    }
}

@Composable
private fun EnterField(
    title: String,
    subTitle: String,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        TitleField(
            title = title,
            subTitle = subTitle
        )
        AppTextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AnswerField(
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    value: Int,
    aqiType: String,
) {
    Surface(
        onClick = { onClick(value) },
        modifier = modifier,
        elevation = 6.dp,
        shape = MaterialTheme.customShapes.small
    ) {
        val color = if (aqiType == AqiTypes.UsAQI.type) AqiColor.usAqiColor(value)
        else AqiColor.euAqiColor(value)
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(10.dp)
                    .background(color)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.small),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}