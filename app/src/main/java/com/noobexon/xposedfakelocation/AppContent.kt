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
    val hasPermissions by viewModel.hasPermissions

    // Check permissions when the composable is first launched
    LaunchedEffect(Unit) {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        viewModel.updatePermissionsStatus(fineLocationGranted)
    }

    if (hasPermissions) {
        MapScreen(viewModel)
    } else {
        PermissionsScreen(viewModel)
    }
}
