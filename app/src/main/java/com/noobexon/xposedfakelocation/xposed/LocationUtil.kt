package com.noobexon.xposedfakelocation.xposed

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

    private val random: Random = Random()

    var lastUpdateTime: Long = 0
    var latitude: Double = 40.7128
    var longitude: Double = -74.0060
    var randomizeRadius: Double = 0.0
    var userAccuracy: Float? = null
    var userAltitude: Double? = null

    fun createMockLocation(originLocation: Location? = null, provider: String = LocationManager.GPS_PROVIDER): Location {
        val location = if (originLocation == null) {
            Location(provider).apply {
                time = System.currentTimeMillis() - 300
            }
        } else {
            Location(originLocation.provider).apply {
                time = originLocation.time
                accuracy = originLocation.accuracy
                bearing = originLocation.bearing
                bearingAccuracyDegrees = originLocation.bearingAccuracyDegrees
                elapsedRealtimeNanos = originLocation.elapsedRealtimeNanos
                verticalAccuracyMeters = originLocation.verticalAccuracyMeters
            }
        }

        location.latitude = latitude
        location.longitude = longitude
        location.speed = 0F
        location.speedAccuracyMetersPerSecond = 0F
        userAccuracy?.let {  location.accuracy = it }
        userAltitude?.let { location.altitude = it }

        try {
            HiddenApiBypass.invoke(location.javaClass, location, "setIsFromMockProvider", false)
            XposedBridge.log("$TAG invoked hidden API - setIsFromMockProvider: false)")
        } catch (e: Exception) {
            XposedBridge.log("$TAG Not possible to mock - ${e.message}")
        }

        return location
    }

    fun updateLocation() {
        try {
            UserPreferences.getLastClickedLocation()?.let {
                lastUpdateTime = System.currentTimeMillis()

                userAccuracy = if (UserPreferences.getUseAccuracy() == true) {
                    (UserPreferences.getAccuracy() ?: DEFAULT_ACCURACY).toFloat()
                } else {
                    null
                }

                userAltitude = if (UserPreferences.getUseAltitude() == true) {
                    UserPreferences.getAltitude() ?: DEFAULT_ALTITUDE
                } else {
                    null
                }

                if (UserPreferences.getUseRandomize() == true) {
                    randomizeRadius = UserPreferences.getRandomizeRadius() ?: DEFAULT_RANDOMIZE_RADIUS
                    val randomLocation = getRandomLocation(it.latitude, it.longitude, randomizeRadius)
                    latitude = randomLocation.first
                    longitude = randomLocation.second
                } else {
                    latitude = it.latitude
                    longitude = it.longitude
                }

                XposedBridge.log("$TAG Updated fake location values to:")
                XposedBridge.log("\t coordinates: (latitude = $latitude, longitude = $longitude)")
                XposedBridge.log("\t accuracy: $userAccuracy")
                XposedBridge.log("\t altitude: $userAltitude")
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

        val newLat = lat + yOffset
        val newLon = lon + xOffset / cos(lat * PI / 180)

        return Pair(newLat, newLon)
    }
}