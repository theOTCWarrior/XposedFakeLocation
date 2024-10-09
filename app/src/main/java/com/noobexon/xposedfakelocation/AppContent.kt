package com.noobexon.xposedfakelocation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        viewModel.updatePermissionsStatus(fineLocationGranted)

        // Mark the permission check as done
        viewModel.markPermissionCheckDone()
    }

    // Show loading or placeholder until the permission check is complete
    if (!isPermissionCheckDone) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()  // Loading indicator or any placeholder content
        }
    } else {
        // Show appropriate screen based on the permission state
        if (hasPermissions) {
            MapScreen(viewModel)
        } else {
            PermissionsScreen(viewModel)
        }
    }
}


