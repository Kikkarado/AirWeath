package ua.airweath.ui.screens.faq

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ua.airweath.R
import ua.airweath.ui.AppState
import ua.airweath.ui.components.TopAppBar
import ua.airweath.ui.theme.spacing

@Composable
fun FAQ(
    appState: AppState
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(title = stringResource(id = R.string.faq)) {
                appState.navController.navigateUp()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
            ) {
                item {
                    Text(
                        text = stringResource(id = R.string.about_aqi),
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Justify
                    )
                }
                item {
                    Text(
                        text = stringResource(id = R.string.about_aqi_1),
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Justify
                    )
                }
                item {
                    Text(
                        text = stringResource(id = R.string.about_aqi_2),
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Justify
                    )
                }
                item {
                    Text(
                        text = stringResource(id = R.string.about_aqi_3),
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Justify
                    )
                }
                item {
                    Text(
                        text = stringResource(id = R.string.about_aqi_4),
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Justify
                    )
                }
                item {
                    Text(
                        text = stringResource(id = R.string.about_aqi_5),
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}