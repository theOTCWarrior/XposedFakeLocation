package com.noobexon.xposedfakelocation.manager.ui.permissions

import android.Manifest
import android.app.Activity
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
import com.noobexon.xposedfakelocation.manager.ui.navigation.Screen
import com.noobexon.xposedfakelocation.manager.ui.permissions.components.PermanentlyDeniedScreen
import com.noobexon.xposedfakelocation.manager.ui.permissions.components.PermissionRequestScreen

@Composable
fun PermissionsScreen(navController: NavController, permissionsViewModel: PermissionsViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    if (activity == null) {
        Text("Error: Unable to access activity.")
        return
    }

    // Observe the permissions state
    val hasPermissions by permissionsViewModel.hasPermissions
    val permanentlyDenied by permissionsViewModel.permanentlyDenied
    val permissionsChecked by permissionsViewModel.permissionsChecked

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
                permissionsViewModel.checkIfPermanentlyDenied(activity)
            }
        }
    )

    // Check permissions when the composable is first displayed
    LaunchedEffect(Unit) {
        permissionsViewModel.checkPermissions(context)
        if (hasPermissions) {
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
