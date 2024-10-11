package com.noobexon.xposedfakelocation.ui.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.core.content.ContextCompat
import com.noobexon.xposedfakelocation.ui.permissions.components.PermanentlyDeniedScreen
import com.noobexon.xposedfakelocation.ui.permissions.components.PermissionRequestScreen
import com.noobexon.xposedfakelocation.ui.navigation.Screen

@Composable
fun PermissionsScreen(navController: NavController, permissionsViewModel: PermissionsViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    if (activity == null) {
        Text("Error: Unable to access activity.")
        return
    }

    // State to track if permissions have been checked
    var permissionsChecked by remember { mutableStateOf(false) }

    // Observe the permissions state
    val hasPermissions by permissionsViewModel.hasPermissions
    val permanentlyDenied by permissionsViewModel.permanentlyDenied

    // Launcher to request permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            permissionsViewModel.updatePermissionsStatus(granted)

            if (granted) {
                // Navigate to MapScreen
                navController.navigate(Screen.Map.route) {
                    popUpTo(Screen.Permissions.route) { inclusive = true }
                }
            } else {
                // Check if permissions were denied permanently
                val shouldShowRationale = activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                permissionsViewModel.updatePermanentlyDenied(!shouldShowRationale)
            }
        }
    )

    // Check permissions when the composable is first displayed
    LaunchedEffect(Unit) {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        permissionsViewModel.updatePermissionsStatus(fineLocationGranted)
        permissionsChecked = true

        if (fineLocationGranted) {
            // Navigate to MapScreen
            navController.navigate(Screen.Map.route) {
                popUpTo(Screen.Permissions.route) { inclusive = true }
            }
        }
    }

    // UI observing permission state
    if (!permissionsChecked) {
        // Show a loading indicator while checking permissions
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (!hasPermissions) {
        // Display the permission request UI
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (permanentlyDenied) {
                    PermanentlyDeniedScreen(context)
                } else {
                    PermissionRequestScreen {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
        }
    }
}
