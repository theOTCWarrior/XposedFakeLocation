package com.noobexon.xposedfakelocation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.noobexon.xposedfakelocation.ui.about.AboutScreen
import com.noobexon.xposedfakelocation.ui.favorites.FavoritesScreen
import com.noobexon.xposedfakelocation.ui.howtouse.HowToUseScreen
import com.noobexon.xposedfakelocation.ui.map.MapScreen
import com.noobexon.xposedfakelocation.ui.map.MapViewModel
import com.noobexon.xposedfakelocation.ui.permissions.PermissionsScreen
import com.noobexon.xposedfakelocation.ui.settings.SettingsScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
) {
    val mapViewModel: MapViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Permissions.route,
    ) {
        composable(route = Screen.Permissions.route) {
            PermissionsScreen(navController = navController)
        }
        composable(route = Screen.Map.route) {
            MapScreen(navController = navController, mapViewModel)
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(navController = navController, mapViewModel)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController = navController)
        }
        composable(route = Screen.HowToUse.route) {
            HowToUseScreen(navController = navController)
        }
        // Add other composable destinations here
    }
}
