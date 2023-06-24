package ua.airweath.ui

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.airweath.MainViewModel
import ua.airweath.auth.SignInResult
import ua.airweath.ui.navigation.Navigation
import ua.airweath.ui.components.Menu
import ua.airweath.ui.theme.drawerShape

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Main(
    appState: AppState,
    viewModel: MainViewModel,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val signInResult by viewModel.signInResult.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.signInWithIntent(result.data ?: return@rememberLauncherForActivityResult)
            }
        }
    )

    LaunchedEffect(signInResult.error) {
        if (!signInResult.error.isNullOrBlank()) {
            Toast.makeText(appState.context, "${signInResult.error}", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                keyboardController?.hide()
                focusManager.clearFocus(true)
            },
        scaffoldState = appState.scaffoldState,
        drawerContent = {
            Menu(
                appState = appState,
                signInResult = signInResult,
                login = {
                    viewModel.signIn(appState.context) { intent ->
                        launcher.launch(
                            IntentSenderRequest.Builder(intent).build()
                        )
                    }
                },
                logout = viewModel::logout,
                signup = {}
            )
        },
        drawerGesturesEnabled = appState.scaffoldState.drawerState.isOpen,
        drawerShape = drawerShape()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Navigation(appState = appState)
        }
    }

}