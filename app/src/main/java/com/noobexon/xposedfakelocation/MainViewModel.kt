package com.noobexon.xposedfakelocation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint

class MainViewModel : ViewModel() {

    // Existing state variables
    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> get() = _isPlaying

    private val _lastClickedLocation = mutableStateOf<GeoPoint?>(null)
    val lastClickedLocation: State<GeoPoint?> get() = _lastClickedLocation

    // State variable for permission status
    private val _hasPermissions = mutableStateOf(false)
    val hasPermissions: State<Boolean> get() = _hasPermissions

    // Toggle the play/stop status
    fun togglePlaying() {
        _isPlaying.value = !_isPlaying.value
    }

    // Update the last clicked location
    fun updateClickedLocation(geoPoint: GeoPoint) {
        _lastClickedLocation.value = geoPoint
    }

    // Update location permission status
    fun updatePermissionsStatus(granted: Boolean) {
        _hasPermissions.value = granted
    }
}
