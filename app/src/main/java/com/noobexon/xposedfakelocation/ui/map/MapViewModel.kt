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
}
