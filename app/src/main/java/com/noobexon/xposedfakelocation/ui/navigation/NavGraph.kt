package com.noobexon.xposedfakelocation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        composable(route = Screen.Map.route) {
            MapScreen(navController = navController)
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen()
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen()
        }
        // Add other composable destinations here
    }
}
