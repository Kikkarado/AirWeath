package ua.airweath.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Calculate
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ua.airweath.R
import ua.airweath.auth.SignInResult
import ua.airweath.ui.AppState
import ua.airweath.ui.navigation.NavRoutes
import ua.airweath.ui.theme.customShapes
import ua.airweath.ui.theme.spacing

@Composable
fun Menu(
    appState: AppState,
    signInResult: SignInResult,
    login: () -> Unit,
    logout: () -> Unit,
    signup: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        elevation = 0.dp,
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(
            topEnd = MaterialTheme.spacing.small,
            bottomEnd = MaterialTheme.spacing.small
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large)
        ) {
            Profile(
                fullName = signInResult.userData?.username ?: "",
                isSingIn = signInResult.userData != null,
                login = login,
                logout = logout,
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.onBackground,
                thickness = 1.dp
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                MenuItem(
                    onClick = {
                        appState.navController.navigate(NavRoutes.Calculate.route) {
                            launchSingleTop = true
                        }.run {
                            scope.launch {
                                appState.scaffoldState.drawerState.close()
                            }
                        }
                    },
                    text = stringResource(id = R.string.calculate_aqi),
                    icon = Icons.Rounded.Calculate
                )
                MenuItem(
                    onClick = {
                        appState.navController.navigate(NavRoutes.Settings.route) {
                            launchSingleTop = true
                        }.run {
                            scope.launch {
                                appState.scaffoldState.drawerState.close()
                            }
                        }
                    },
                    text = stringResource(id = R.string.settings),
                    icon = Icons.Rounded.Settings
                )
                MenuItem(
                    onClick = {
                        appState.navController.navigate(NavRoutes.FAQ.route) {
                            launchSingleTop = true
                        }.run {
                            scope.launch {
                                appState.scaffoldState.drawerState.close()
                            }
                        }
                    },
                    text = stringResource(id = R.string.faq),
                    icon = Icons.Rounded.Quiz
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Profile(
    fullName: String,
    isSingIn: Boolean,
    login: () -> Unit,
    logout: () -> Unit,
) {
    if (isSingIn) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            Text(
                text = fullName,
                modifier = Modifier,
                style = MaterialTheme.typography.h6
            )
            AppTextIconButton(
                onClick = logout,
                modifier = Modifier,
                text = stringResource(id = R.string.logout),
                icon = Icons.Rounded.Logout,
                iconTint = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onBackground,
                elevation = 6.dp,
                alignment = Alignment.CenterHorizontally
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            Text(
                text = stringResource(id = R.string.login_with_google),
                modifier = Modifier,
                style = MaterialTheme.typography.h6
            )
            FlowRow(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                verticalAlignment = Alignment.CenterVertically,
                maxItemsInEachRow = 2
            ) {
                ServiceButton(
                    onClick = login,
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f),
                    text = stringResource(id = R.string.google),
                    textStyle = MaterialTheme.typography.h5,
                    textModifier = Modifier
                        .fillMaxWidth(),
                    icon = painterResource(id = R.drawable.ic_google_logo),
                    iconTint = Color.Unspecified,
                    contentColor = MaterialTheme.colors.primary,
                    background = MaterialTheme.colors.surface,
                    elevation = 6.dp,
                )
            }
        }
    }
}

@Composable
private fun MenuItem(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
) {
    AppTextIconButton(
        onClick = onClick,
        text = text,
        icon = icon,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        iconTint = MaterialTheme.colors.primary,
        padding = MaterialTheme.spacing.small,
        shape = MaterialTheme.customShapes.zero,
        pressedElevation = false
    )
}