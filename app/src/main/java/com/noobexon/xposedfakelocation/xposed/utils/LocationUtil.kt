// LocationUtil.kt
package com.noobexon.xposedfakelocation.xposed.utils

import android.location.Location
import android.location.LocationManager
import com.noobexon.xposedfakelocation.data.DEFAULT_ACCURACY
import com.noobexon.xposedfakelocation.data.DEFAULT_ALTITUDE
import com.noobexon.xposedfakelocation.data.DEFAULT_RANDOMIZE_RADIUS
import com.noobexon.xposedfakelocation.data.PI
import com.noobexon.xposedfakelocation.data.RADIUS_EARTH
import de.robv.android.xposed.XposedBridge
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.util.Random
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LocationUtil {
    private const val TAG = "[LocationUtil]"

    private const val DEBUG: Boolean = true

    private val random: Random = Random()

    var fakeLatitude: Double = 0.0
    var fakeLongitude: Double = 0.0
    var fakeAccuracy: Float = 0F
    var fakeAltitude: Double = 0.0
    var randomizationRadius: Double = 0.0

    @Synchronized
    fun createFakeLocation(originalLocation: Location? = null, provider: String = LocationManager.GPS_PROVIDER): Location {
        val fakeLocation = if (originalLocation == null) {
            Location(provider).apply {
                time = System.currentTimeMillis() - 300
            }
        } else {
            Location(originalLocation.provider).apply {
                time = originalLocation.time
                accuracy = originalLocation.accuracy
                bearing = originalLocation.bearing
                bearingAccuracyDegrees = originalLocation.bearingAccuracyDegrees
                elapsedRealtimeNanos = originalLocation.elapsedRealtimeNanos
                verticalAccuracyMeters = originalLocation.verticalAccuracyMeters
            }
        }

        fakeLocation.latitude = fakeLatitude
        fakeLocation.longitude = fakeLongitude

        if (fakeAccuracy != 0F) {
            fakeLocation.accuracy = fakeAccuracy
        }

        if (fakeAltitude != 0.0) {
            fakeLocation.altitude = fakeAltitude
        }

        fakeLocation.speed = 0F
        fakeLocation.speedAccuracyMetersPerSecond = 0F

        attemptHideMockProvider(fakeLocation)

        return fakeLocation
    }

    private fun attemptHideMockProvider(fakeLocation: Location) {
        try {
            HiddenApiBypass.invoke(fakeLocation.javaClass, fakeLocation, "setIsFromMockProvider", false)
            XposedBridge.log("$TAG invoked hidden API - setIsFromMockProvider: false)")
        } catch (e: Exception) {
            XposedBridge.log("$TAG Not possible to mock - ${e.message}")
        }
    }

    @Synchronized
    fun updateLocation() {
        try {
            PreferencesUtil.getLastClickedLocation()?.let {
                if (PreferencesUtil.getUseAccuracy() == true) {
                    fakeAccuracy = (PreferencesUtil.getAccuracy() ?: DEFAULT_ACCURACY).toFloat()
                }

                 if (PreferencesUtil.getUseAltitude() == true) {
                     fakeAltitude = PreferencesUtil.getAltitude() ?: DEFAULT_ALTITUDE
                }

                if (PreferencesUtil.getUseRandomize() == true) {
                    randomizationRadius = PreferencesUtil.getRandomizeRadius() ?: DEFAULT_RANDOMIZE_RADIUS
                    val randomLocation = getRandomLocation(it.latitude, it.longitude, randomizationRadius)
                    fakeLatitude = randomLocation.first
                    fakeLongitude = randomLocation.second
                } else {
                    fakeLatitude = it.latitude
                    fakeLongitude = it.longitude
                }

                if (DEBUG) {
                    XposedBridge.log("$TAG Updated fake location values to:")
                    XposedBridge.log("\t coordinates: (latitude = $fakeLatitude, longitude = $fakeLongitude)")
                    XposedBridge.log("\t accuracy: $fakeAccuracy")
                    XposedBridge.log("\t altitude: $fakeAltitude")
                }
            } ?: XposedBridge.log("$TAG Last clicked location is null")
        } catch (e: Exception) {
            XposedBridge.log("$TAG Error - ${e.message}")
        }
    }

    // Calculates a random point within a circle around the fake location that has the radius set by by the user.
    private fun getRandomLocation(lat: Double, lon: Double, radiusInMeters: Double): Pair<Double, Double> {
        val radiusInDegrees = radiusInMeters / RADIUS_EARTH * (180 / PI)

        val u = random.nextDouble()
        val v = random.nextDouble()
        val w = radiusInDegrees * sqrt(u)
        val t = 2 * PI * v
        val xOffset = w * cos(t)
        val yOffset = w * sin(t)

        var newLat = (lat + yOffset).coerceIn(-90.0, 90.0)
        var newLon = lon + xOffset / cos(lat * PI / 180)
        newLon = ((newLon + 180) % 360 + 360) % 360 - 180   // Normalize longitude to -180 to 180

        return Pair(newLat, newLon)
    }
}