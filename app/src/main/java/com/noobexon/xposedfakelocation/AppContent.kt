// AppContent.kt
package com.noobexon.xposedfakelocation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppContent(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current

    // Observe the hasLocationPermission state from the ViewModel
    val hasLocationPermission by viewModel.hasPermissions

    // Check permissions when the composable is first launched
    LaunchedEffect(Unit) {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val granted = fineLocationGranted || coarseLocationGranted
        viewModel.updatePermissionsStatus(granted)
    }

    if (hasLocationPermission) {
        MapScreen(viewModel)
    } else {
        PermissionsScreen(viewModel)
    }
}
