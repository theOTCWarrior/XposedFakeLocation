package com.noobexon.xposedfakelocation.ui.permissions

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class PermissionsViewModel : ViewModel() {

    // State variable for permission status
    private val _hasPermissions = mutableStateOf(false)
    val hasPermissions: State<Boolean> get() = _hasPermissions

    // State variable for tracking whether permissions are permanently denied
    private val _permanentlyDenied = mutableStateOf(false)
    val permanentlyDenied: State<Boolean> get() = _permanentlyDenied

    // Update location permission status
    fun updatePermissionsStatus(granted: Boolean) {
        _hasPermissions.value = granted
    }

    // Update permanent denial status
    fun updatePermanentlyDenied(denied: Boolean) {
        _permanentlyDenied.value = denied
    }
}
