package com.noobexon.xposedfakelocation.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.noobexon.xposedfakelocation.MainViewModel

@Composable
fun PermissionsScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity  // Get the current activity

    // Launcher to request permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            viewModel.updatePermissionsStatus(granted)

            // Check if permissions were denied permanently
            if (!granted && activity != null) {  // Ensure activity is not null
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (viewModel.permanentlyDenied.value) {
                Text(
                    text = "You have permanently denied location permissions. Please enable them from settings and restart the app.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    // Open app settings
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Open Settings")
                }
            } else {
                Text(
                    text = "Permissions are required to use this app!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    // Request permissions again
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }) {
                    Text("Grant Permissions")
                }
            }
        }
    }
}


