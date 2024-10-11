package com.noobexon.xposedfakelocation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.noobexon.xposedfakelocation.ui.favorites.FavoritesScreen
import com.noobexon.xposedfakelocation.ui.map.MapScreen
import com.noobexon.xposedfakelocation.ui.permissions.PermissionsScreen
import com.noobexon.xposedfakelocation.ui.settings.SettingsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Permissions.route,
    ) {
        composable(route = Screen.Permissions.route) {
            PermissionsScreen(navController = navController)
        }
        composable(route = Screen.Map.route) {
            MapScreen(navController = navController)
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        // Add other composable destinations here
    }
}
