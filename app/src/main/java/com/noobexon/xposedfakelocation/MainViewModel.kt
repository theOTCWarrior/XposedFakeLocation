package com.noobexon.xposedfakelocation

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import org.osmdroid.util.GeoPoint

class MainViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context: Context = getApplication()

    // SharedPreferences
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)

    // Existing state variables
    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> get() = _isPlaying

    private val _lastClickedLocation = mutableStateOf<GeoPoint?>(null)
    val lastClickedLocation: State<GeoPoint?> get() = _lastClickedLocation

    // State variable for permission status
    private val _hasPermissions = mutableStateOf(false)
    val hasPermissions: State<Boolean> get() = _hasPermissions

    // State variable to track if permission check is done
    private val _isPermissionCheckDone = mutableStateOf(false)
    val isPermissionCheckDone: State<Boolean> get() = _isPermissionCheckDone

    // State variable for tracking whether permissions are permanently denied
    private val _permanentlyDenied = mutableStateOf(false)
    val permanentlyDenied: State<Boolean> get() = _permanentlyDenied

    // Toggle the play/stop status
    fun togglePlaying() {
        _isPlaying.value = !_isPlaying.value

        if (!_isPlaying.value) {
            // Transitioned to "not playing" state
            // Remove the marker by setting lastClickedLocation to null
            updateClickedLocation(null)
        }

        // Save the state to SharedPreferences
        sharedPrefs.edit()
            .putBoolean(KEY_IS_PLAYING, _isPlaying.value)
            .apply()
    }

    // Update the last clicked location
    fun updateClickedLocation(geoPoint: GeoPoint?) {
        _lastClickedLocation.value = geoPoint

        // Save the location to SharedPreferences if not null
        geoPoint?.let {
            sharedPrefs.edit()
                .putFloat(KEY_LATITUDE, it.latitude.toFloat())
                .putFloat(KEY_LONGITUDE, it.longitude.toFloat())
                .apply()
        } ?: run {
            // Remove the location keys when the location is null
            sharedPrefs.edit()
                .remove(KEY_LATITUDE)
                .remove(KEY_LONGITUDE)
                .apply()
        }
    }

    // Update location permission status
    fun updatePermissionsStatus(granted: Boolean) {
        _hasPermissions.value = granted
    }

    // Update permanent denial status
    fun updatePermanentlyDenied(denied: Boolean) {
        _permanentlyDenied.value = denied
    }

    // Mark permission check as done
    fun markPermissionCheckDone() {
        _isPermissionCheckDone.value = true
    }
}
