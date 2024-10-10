package com.noobexon.xposedfakelocation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noobexon.xposedfakelocation.ui.map.MapScreen
import com.noobexon.xposedfakelocation.ui.permissions.PermissionsScreen
import com.noobexon.xposedfakelocation.ui.permissions.PermissionsViewModel

@Composable
fun AppContent(
    permissionsViewModel: PermissionsViewModel = viewModel()
) {
    // Get the context
    val context = LocalContext.current

    // Observe the hasLocationPermission state from the ViewModel
    val hasPermissions by permissionsViewModel.hasPermissions
    val isPermissionsCheckDone by permissionsViewModel.isPermissionsCheckDone

    // Check permissions when the composable is first launched
    LaunchedEffect(Unit) {
        val fineLocationGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        permissionsViewModel.updatePermissionsStatus(fineLocationGranted)
        permissionsViewModel.markPermissionCheckDone()
    }

    // Navigate when possible to the required screen.
    if (isPermissionsCheckDone) {
        if (hasPermissions) {
            MapScreen()
        } else {
            PermissionsScreen(permissionsViewModel)
        }
    }
}


