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
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LocationUtil {
    private const val TAG = "[LocationUtil]"

    private const val DEBUG: Boolean = false

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

    // Calculates a random point within a circle around the fake location that has the radius set by by the user. Uses Haversine's formula.
    private fun getRandomLocation(lat: Double, lon: Double, radiusInMeters: Double): Pair<Double, Double> {
        val radiusInRadians = radiusInMeters / RADIUS_EARTH

        val latRad = Math.toRadians(lat)
        val lonRad = Math.toRadians(lon)

        val sinLat = sin(latRad)
        val cosLat = cos(latRad)

        // Generate two random numbers
        val rand1 = random.nextDouble()
        val rand2 = random.nextDouble()

        // Random distance and bearing
        val distance = radiusInRadians * sqrt(rand1)
        val bearing = 2 * PI * rand2

        val sinDistance = sin(distance)
        val cosDistance = cos(distance)

        val newLatRad = asin(sinLat * cosDistance + cosLat * sinDistance * cos(bearing))
        val newLonRad = lonRad + atan2(
            sin(bearing) * sinDistance * cosLat,
            cosDistance - sinLat * sin(newLatRad)
        )

        // Convert back to degrees
        val newLat = Math.toDegrees(newLatRad)
        var newLon = Math.toDegrees(newLonRad)

        // Normalize longitude to be between -180 and 180 degrees
        newLon = ((newLon + 180) % 360 + 360) % 360 - 180

        // Clamp latitude to -90 to 90 degrees
        val finalLat = newLat.coerceIn(-90.0, 90.0)

        return Pair(finalLat, newLon)
    }
}