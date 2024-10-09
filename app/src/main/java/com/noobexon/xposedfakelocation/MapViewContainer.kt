package com.noobexon.xposedfakelocation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

@Composable
fun MapViewContainer(viewModel: MainViewModel) {
    val context = LocalContext.current

    val isLoading by viewModel.isLoading

    // Remember the MapView
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(false)
            setMultiTouchControls(true)
        }
    }

    // Remember the user's marker
    val userMarker = remember {
        Marker(mapView).apply {
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
    }

    // Remember the location overlay
    val locationOverlay = remember {
        MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
            enableMyLocation()
        }
    }

    // Add the location overlay to the map
    LaunchedEffect(Unit) {
        if (!mapView.overlays.contains(locationOverlay)) {
            mapView.overlays.add(locationOverlay)
        }
    }

    // Observe the last clicked location and isPlaying state
    val lastClickedLocation by viewModel.lastClickedLocation
    val isPlaying by viewModel.isPlaying

    // Handle marker visibility and position
    LaunchedEffect(lastClickedLocation) {
        if (lastClickedLocation != null) {
            // Add the marker to the map if not already added
            if (!mapView.overlays.contains(userMarker)) {
                mapView.overlays.add(userMarker)
            }

            userMarker.position = lastClickedLocation
            mapView.controller.animateTo(lastClickedLocation)
            mapView.invalidate()

            val message = "Latitude: ${lastClickedLocation!!.latitude}\nLongitude: ${lastClickedLocation!!.longitude}"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else {
            // Remove the marker from the map if it exists
            if (mapView.overlays.contains(userMarker)) {
                mapView.overlays.remove(userMarker)
                mapView.invalidate()
            }
        }
    }

    // Set up the map click listener
    DisposableEffect(mapView, isPlaying) {
        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                if (!isPlaying) {
                    viewModel.updateClickedLocation(p)
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }

        val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
        mapView.overlays.add(mapEventsOverlay)

        onDispose {
            // Remove the overlay when the effect is disposed
            mapView.overlays.remove(mapEventsOverlay)
        }
    }

    // Center the map on the user's current location when it's available
    LaunchedEffect(locationOverlay) {
        // Wait until the user's location is available or a timeout occurs
        val maxAttempts = 80 // e.g., 80 attempts * 100ms = 8 seconds
        var attempts = 0
        while (locationOverlay.myLocation == null && attempts < maxAttempts) {
            delay(100) // Wait for 100ms before checking again
            attempts++
        }
        val userLocation = locationOverlay.myLocation
        userLocation?.let { geoPoint ->
            viewModel.updateUserLocation(geoPoint) // Update the user's location in the ViewModel
            mapView.controller.setZoom(18.0) // Adjust zoom level as desired
            mapView.controller.animateTo(geoPoint)
            viewModel.setLoadingFinished() // Mark the loading as finished
        } ?: run {
            // If location is not available after timeout, set default location
            mapView.controller.setZoom(18.0)
            mapView.controller.setCenter(GeoPoint(0.0, 0.0))
            viewModel.setLoadingFinished() // Mark loading as finished
        }
    }

    // Handle MapView lifecycle
    DisposableEffect(Unit) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
            locationOverlay.disableMyLocation()
        }
    }

    // Display a loading spinner or the MapView based on the loading state
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator() // Show a loading spinner while waiting
        }
    } else {
        // Display the MapView when loading is finished
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
    }
}
