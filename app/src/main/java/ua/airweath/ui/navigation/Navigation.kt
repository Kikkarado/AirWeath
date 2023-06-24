package ua.airweath.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.android.gms.maps.model.LatLng
import ua.airweath.ui.AppState
import ua.airweath.ui.screens.calculate.Calculate
import ua.airweath.ui.screens.faq.FAQ
import ua.airweath.ui.screens.home.Home
import ua.airweath.ui.screens.places.Places
import ua.airweath.ui.screens.searchplace.SearchPlace
import ua.airweath.ui.screens.statistics.Statistics
import ua.airweath.ui.screens.settings.Settings

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(
    appState: AppState,
) {
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = NavRoutes.Home().route
    ) {

        composable(
            route = NavRoutes.Home().route,
            arguments = listOf(
                navArgument("placeUUID") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            Home(appState = appState, placeUUID = backStackEntry.arguments?.getString("placeUUID"))
        }

        composable(route = NavRoutes.Settings.route) {
            Settings(appState = appState)
        }

        composable(
            route = NavRoutes.Statistics().route,
            arguments = listOf(
                navArgument("placeUUID") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            Statistics(
                appState = appState,
                placeUUID = backStackEntry.arguments?.getString("placeUUID") ?: return@composable
            )
        }

        composable(
            route = NavRoutes.SearchPlace().route,
            arguments = listOf(
                navArgument("latitude") {
                    type = NavType.StringType
                },
                navArgument("longitude") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val latLng = LatLng(arguments.getString("latitude")?.toDoubleOrNull()?:.0, arguments.getString("longitude")?.toDoubleOrNull()?:.0)
            SearchPlace(
                appState = appState,
                latLng = latLng
            )
        }

        composable(route = NavRoutes.Calculate.route) {
            Calculate(appState = appState)
        }

        composable(route = NavRoutes.Places.route) {
            Places(appState = appState)
        }

        composable(
            route = NavRoutes.FAQ.route
        ) {
            FAQ(appState = appState)
        }

    }

}