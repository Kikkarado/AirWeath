package ua.airweath.ui.navigation

sealed class NavRoutes(val  route: String) {

    class Home(placeUUID: String = "{placeUUID}") : NavRoutes("home?$placeUUID")

    object Settings: NavRoutes("settings")

    class Statistics(placeUUID: String = "{placeUUID}") : NavRoutes("statistics?$placeUUID")

    class SearchPlace(latitude: String = "{latitude}", longitude: String = "{longitude}") : NavRoutes("search_place/$latitude&$longitude")

    object Calculate : NavRoutes("calculate")

    object Places : NavRoutes("places")

    object FAQ : NavRoutes("faq")

}
