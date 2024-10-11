package com.noobexon.xposedfakelocation.ui.permissions

import android.app.Activity
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.core.content.ContextCompat

class PermissionsViewModel : ViewModel() {

    // State variable for permission status
    private val _hasPermissions = mutableStateOf(false)
    val hasPermissions: State<Boolean> get() = _hasPermissions

    // State variable for tracking whether permissions are permanently denied
    private val _permanentlyDenied = mutableStateOf(false)
    val permanentlyDenied: State<Boolean> get() = _permanentlyDenied

    // State variable for tracking if permissions have been checked
    private val _permissionsChecked = mutableStateOf(false)
    val permissionsChecked: State<Boolean> get() = _permissionsChecked

    // Function to check permissions and update the state accordingly
    fun checkPermissions(context: Context) {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        _hasPermissions.value = fineLocationGranted
        _permissionsChecked.value = true
    }

    // Update permissions status when user responds to the permission request
    fun updatePermissionsStatus(granted: Boolean) {
        _hasPermissions.value = granted
    }

    // Update permanent denial of permissions status
    fun updatePermanentlyDenied(denied: Boolean) {
        _permanentlyDenied.value = denied
    }

    // Check if permission is permanently denied
    fun checkIfPermanentlyDenied(activity: Activity) {
        val shouldShowRationale = activity.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
        _permanentlyDenied.value = !shouldShowRationale
    }
}