package com.noobexon.xposedfakelocation.screens

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.noobexon.xposedfakelocation.MainViewModel

@Composable
fun PermissionsScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity  ?: return // Ensures the activity is non-null

    // Launcher to request permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            viewModel.updatePermissionsStatus(granted)

            // Check if permissions were denied permanently
            if (!granted) {
                val shouldShowRationale = activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                viewModel.updatePermanentlyDenied(!shouldShowRationale)
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
            if (viewModel.permanentlyDenied.value) {
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


