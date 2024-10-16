package com.noobexon.xposedfakelocation.ui.navigation

sealed class Screen(val route: String) {
    object Permissions : Screen("permissions")
    object Map : Screen("map")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
    object About : Screen("about")
    object HowToUse : Screen("howtouse")
}
