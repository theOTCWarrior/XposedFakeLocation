package com.noobexon.xposedfakelocation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.noobexon.xposedfakelocation.ui.favorites.FavoritesScreen
import com.noobexon.xposedfakelocation.ui.map.MapScreen
import com.noobexon.xposedfakelocation.ui.permissions.PermissionsScreen
import com.noobexon.xposedfakelocation.ui.settings.SettingsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Permissions.route,
        modifier = modifier
    ) {
        composable(route = Screen.Permissions.route) {
            PermissionsScreen(navController = navController)
        }
        composable(
            route = Screen.Map.route + "?latitude={latitude}&longitude={longitude}",
            arguments = listOf(
                navArgument("latitude") {
                    type = NavType.FloatType
                    defaultValue = Float.NaN
                },
                navArgument("longitude") {
                    type = NavType.FloatType
                    defaultValue = Float.NaN
                }
            )
        ) { backStackEntry ->
            val latitude = backStackEntry.arguments?.getFloat("latitude") ?: Float.NaN
            val longitude = backStackEntry.arguments?.getFloat("longitude") ?: Float.NaN
            MapScreen(
                navController = navController,
                initialLatitude = latitude.toDouble(),
                initialLongitude = longitude.toDouble()
            )
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen()
        }
        // Add other composable destinations here
    }
}
