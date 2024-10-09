package com.noobexon.xposedfakelocation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noobexon.xposedfakelocation.screens.MapScreen
import com.noobexon.xposedfakelocation.screens.PermissionsScreen

@Composable
fun AppContent(viewModel: MainViewModel = viewModel()) {
    // Get the context
    val context = LocalContext.current

    // Observe the hasLocationPermission state from the ViewModel
    val hasPermissions by viewModel.hasPermissions
    val isPermissionCheckDone by viewModel.isPermissionCheckDone

    // Check permissions when the composable is first launched
    LaunchedEffect(Unit) {
        val fineLocationGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        viewModel.updatePermissionsStatus(fineLocationGranted)
        viewModel.markPermissionCheckDone()
    }

    // Navigate when possible to the required screen.
    if (isPermissionCheckDone) {
        if (hasPermissions) {
            MapScreen(viewModel)
        } else {
            PermissionsScreen(viewModel)
        }
    }
}


