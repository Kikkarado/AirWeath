@file:OptIn(ExperimentalAnimationApi::class)

package ua.airweath.ui

import android.content.Context
import android.content.res.Resources
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

data class AppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    val resources: Resources,
    val context: Context,
) {

    @Composable
    fun ShowSnackBar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        /*TODO: snackbar content*/
    }

}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberAnimatedNavController(),
    resources: Resources = LocalContext.current.resources,
    context: Context = LocalContext.current,
) = remember(scaffoldState, navController, resources, context) {
    AppState(scaffoldState, navController, resources, context)
}