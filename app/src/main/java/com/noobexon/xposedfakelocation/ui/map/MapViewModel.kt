package com.noobexon.xposedfakelocation.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.noobexon.xposedfakelocation.data.model.FavoriteLocation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import com.noobexon.xposedfakelocation.data.repository.PreferencesRepository

class MapViewModel(application: Application) : AndroidViewModel(application) {

    // App's shared preferences
    private val preferencesRepository = PreferencesRepository(application)

    // State variable for the play FAB
    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> get() = _isPlaying

    // State variable for coordinates of the last clicked point in the map
    private val _lastClickedLocation = mutableStateOf<GeoPoint?>(null)
    val lastClickedLocation: State<GeoPoint?> get() = _lastClickedLocation

    // State to store the user's location
    private val _userLocation = mutableStateOf<GeoPoint?>(null)
    val userLocation: State<GeoPoint?> get() = _userLocation

    // State to manage loading status
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> get() = _isLoading

    // Add a new state variable for the go-to point event
    private val _goToPointEvent = MutableSharedFlow<GeoPoint>()
    val goToPointEvent: SharedFlow<GeoPoint> get() = _goToPointEvent.asSharedFlow()

    private val _centerMapEvent = MutableSharedFlow<Unit>()
    val centerMapEvent: SharedFlow<Unit> get() = _centerMapEvent.asSharedFlow()

    // Add new state variables for showing dialogs
    private val _showGoToPointDialog = mutableStateOf(false)
    val showGoToPointDialog: State<Boolean> get() = _showGoToPointDialog

    private val _showAddToFavoritesDialog = mutableStateOf(false)
    val showAddToFavoritesDialog: State<Boolean> get() = _showAddToFavoritesDialog

    // State variables for GoToPointDialog
    private val _latitudeInput = mutableStateOf("")
    val latitudeInput: State<String> get() = _latitudeInput

    private val _longitudeInput = mutableStateOf("")
    val longitudeInput: State<String> get() = _longitudeInput

    private val _latitudeError = mutableStateOf<String?>(null)
    val latitudeError: State<String?> get() = _latitudeError

    private val _longitudeError = mutableStateOf<String?>(null)
    val longitudeError: State<String?> get() = _longitudeError

    // FAB clickability
    val isFabClickable: Boolean
        get() = lastClickedLocation.value != null

    // Toggle the play/stop status
    fun togglePlaying() {
        _isPlaying.value = !_isPlaying.value
        if (!_isPlaying.value) {
            updateClickedLocation(null)
        }
        preferencesRepository.saveIsPlaying(_isPlaying.value)
    }

    // Updates the user's current location
    fun updateUserLocation(location: GeoPoint) {
        _userLocation.value = location
    }

    // Update the last clicked location
    fun updateClickedLocation(geoPoint: GeoPoint?) {
        _lastClickedLocation.value = geoPoint

        geoPoint?.let {
            preferencesRepository.saveLastClickedLocation(
                it.latitude.toFloat(),
                it.longitude.toFloat()
            )
        } ?: run {
            preferencesRepository.clearLastClickedLocation()
        }
    }

    fun addFavoriteLocation(favoriteLocation: FavoriteLocation) {
        preferencesRepository.addFavorite(favoriteLocation)
    }

    // Add the function to handle go to point
    fun goToPoint(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val geoPoint = GeoPoint(latitude, longitude)
            _goToPointEvent.emit(geoPoint)
        }
    }

    // Function to trigger the event
    fun triggerCenterMapEvent() {
        viewModelScope.launch {
            _centerMapEvent.emit(Unit)
        }
    }

    fun setLoadingFinished() {
        _isLoading.value = false
    }

    // Functions to handle showing and hiding dialogs
    fun showGoToPointDialog() {
        _showGoToPointDialog.value = true
    }

    fun hideGoToPointDialog() {
        _showGoToPointDialog.value = false
    }

    fun showAddToFavoritesDialog() {
        _showAddToFavoritesDialog.value = true
    }

    fun hideAddToFavoritesDialog() {
        _showAddToFavoritesDialog.value = false
    }

    // Update latitude input
    fun updateLatitudeInput(input: String) {
        _latitudeInput.value = input
        _latitudeError.value = null // Reset error when input changes
    }

    // Update longitude input
    fun updateLongitudeInput(input: String) {
        _longitudeInput.value = input
        _longitudeError.value = null // Reset error when input changes
    }

    // Validate inputs and handle "Go" action
    fun validateAndGo(onSuccess: (latitude: Double, longitude: Double) -> Unit) {
        val latitude = _latitudeInput.value.toDoubleOrNull()
        val longitude = _longitudeInput.value.toDoubleOrNull()
        var isValid = true

        if (latitude == null || latitude !in -90.0..90.0) {
            _latitudeError.value = "Latitude must be between -90 and 90"
            isValid = false
        }

        if (longitude == null || longitude !in -180.0..180.0) {
            _longitudeError.value = "Longitude must be between -180 and 180"
            isValid = false
        }

        if (isValid) {
            onSuccess(latitude!!, longitude!!)
        }
    }

    // Clear input fields and errors
    fun clearGoToPointInputs() {
        _latitudeInput.value = ""
        _longitudeInput.value = ""
        _latitudeError.value = null
        _longitudeError.value = null
    }
}
