package com.noobexon.xposedfakelocation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay

@Composable
fun MapViewContainer(viewModel: MainViewModel) {
    val context = LocalContext.current

    // Observe the last clicked location
    val lastClickedLocation by viewModel.lastClickedLocation

    // Show a toast when the last clicked location changes
    LaunchedEffect(lastClickedLocation) {
        lastClickedLocation?.let { geoPoint ->
            val message = "Latitude: ${geoPoint.latitude}\nLongitude: ${geoPoint.longitude}"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(false)
            setMultiTouchControls(true)

            controller.setZoom(2.0)
            controller.setCenter(GeoPoint(0.0, 0.0))

            // Map click listener
            val mapEventsReceiver = object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                    // Update the ViewModel with the clicked location
                    viewModel.updateClickedLocation(p)
                    return true
                }

                override fun longPressHelper(p: GeoPoint): Boolean {
                    // Will be used later
                    return false
                }
            }
            val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
            overlays.add(mapEventsOverlay)
        }
    }

    // Handle MapView lifecycle
    DisposableEffect(Unit) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
        }
    }

    // Display the MapView
    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}
