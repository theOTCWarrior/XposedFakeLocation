package com.noobexon.xposedfakelocation.ui.permissions

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noobexon.xposedfakelocation.ui.permissions.components.PermanentlyDeniedScreen
import com.noobexon.xposedfakelocation.ui.permissions.components.PermissionRequestScreen

@Composable
fun PermissionsScreen(permissionsViewModel: PermissionsViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity // Ensures the activity is non-null

    if (activity == null) {
        Text("Error: Unable to access activity.")
        return
    }

    // Launcher to request permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            permissionsViewModel.updatePermissionsStatus(granted)

            // Check if permissions were denied permanently
            if (!granted) {
                val shouldShowRationale = activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                permissionsViewModel.updatePermanentlyDenied(!shouldShowRationale)
            }
        }
    )

    // UI observing permission state
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (permissionsViewModel.permanentlyDenied.value) {
                PermanentlyDeniedScreen(context)
            } else {
                PermissionRequestScreen {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        }
    }
}
