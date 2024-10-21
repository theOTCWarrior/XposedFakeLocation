package com.noobexon.xposedfakelocation.xposed.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.annotation.RequiresApi
import com.noobexon.xposedfakelocation.data.DEFAULT_ACCURACY
import com.noobexon.xposedfakelocation.data.DEFAULT_ALTITUDE
import com.noobexon.xposedfakelocation.data.DEFAULT_RANDOMIZE_RADIUS
import com.noobexon.xposedfakelocation.data.RADIUS_EARTH
import com.noobexon.xposedfakelocation.data.PI
import com.noobexon.xposedfakelocation.xposed.UserPreferences
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationApiHooks(val appContext: Context, val appLpparam: LoadPackageParam) {
    private val tag = "[LocationApiHooks]"

    private val random: Random = Random()

    private var lastUpdateTime: Long = 0
    private var latitude: Double = 40.7128
    private var longitude: Double = -74.0060
    private var randomizeRadius: Double = 0.0
    private var userAccuracy: Float? = null
    private var userAltitude: Double? = null

    @RequiresApi(Build.VERSION_CODES.S)
    fun initHooks() {
        hookLocationAPI()
        XposedBridge.log("$tag Instantiated hooks successfully")
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hookLocationAPI() {
        updateLocation()
        if (appLpparam.packageName == "android") {
            hookSystemServices(appLpparam.classLoader)
        }
        hookLocationClass(appLpparam.classLoader)
        hookLocationManager(appLpparam.classLoader)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun hookSystemServices(classLoader: ClassLoader) {
        try {
            val locationManagerServiceClass = XposedHelpers.findClass("com.android.server.LocationManagerService", classLoader)

            XposedHelpers.findAndHookMethod(
                locationManagerServiceClass,
                "getLastLocation",
                LocationRequest::class.java,
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        XposedBridge.log("$tag [SystemHook] Entered method getLastLocation(locationRequest, packageName)")
                        XposedBridge.log("\t Request comes from: ${param.args[1] as String}")
                        val location = createMockLocation()
                        param.result = location
                        XposedBridge.log("\t Modified to: $location (original method not executed)")
                    }
                })

            val methodsToReplace = arrayOf(
                "addGnssBatchingCallback",
                "addGnssMeasurementsListener",
                "addGnssNavigationMessageListener"
            )

            for (methodName in methodsToReplace) {
                XposedHelpers.findAndHookMethod(
                    locationManagerServiceClass,
                    methodName,
                    XC_MethodReplacement.returnConstant(false)
                )
            }


            XposedHelpers.findAndHookMethod(
                XposedHelpers.findClass("com.android.server.LocationManagerService\$Receiver", classLoader),
                "callLocationChangedLocked",
                Location::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        XposedBridge.log("$tag [SystemHook] Entered method callLocationChangedLocked(location)")
                        val location = createMockLocation(param.args[0] as? Location)
                        param.args[0] = location
                        XposedBridge.log("\t Modified to: $location")
                    }
                })

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking system services")
            XposedBridge.log(e)
        }
    }

    private fun hookLocationClass(classLoader: ClassLoader) {
        try {
            val locationClass = XposedHelpers.findClass("android.location.Location", classLoader)

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getLatitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        XposedBridge.log("$tag Entered method getLatitude()")
                        param.result = latitude
                        XposedBridge.log("\t Modified to: $latitude (original method not executed)")
                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getLongitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        XposedBridge.log("$tag Entered method getLongitude()")
                        param.result = longitude
                        XposedBridge.log("\t Modified to: $longitude (original method not executed)")

                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getAccuracy",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        XposedBridge.log("$tag Entered method getAccuracy()")
                        XposedBridge.log("\t Should modify: ${UserPreferences.getUseAccuracy()}")
                        if (userAccuracy != null) {
                            param.result = userAccuracy!!
                            XposedBridge.log("\t Modified to: $userAccuracy (original method not executed)")
                        }
                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getAltitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        XposedBridge.log("$tag Entered method getAltitude()")
                        XposedBridge.log("\t Should modify: ${UserPreferences.getUseAltitude()}")
                        if (userAltitude != null) {
                            param.result = userAltitude!!
                            XposedBridge.log("\t Modified to: $userAltitude (original method not executed)")
                        }
                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "set",
                Location::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        XposedBridge.log("$tag Entered method set(location)")
                        val location = createMockLocation(param.args[0] as? Location)
                        param.args[0] = location
                        XposedBridge.log("\t Modified to: $location")
                    }
                })

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking Location class - ${e.message}")
        }
    }

    private fun hookLocationManager(classLoader: ClassLoader) {
        try {
            val locationManagerClass = XposedHelpers.findClass("android.location.LocationManager", classLoader)

            XposedHelpers.findAndHookMethod(
                locationManagerClass,
                "getLastKnownLocation",
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        XposedBridge.log("$tag Entered method getLastKnownLocation(provider)")
                        val provider = param.args[0] as String
                        XposedBridge.log("\t Requested provider: $provider")
                        val location = createMockLocation(provider = provider)
                        param.result = location
                        XposedBridge.log("\t Modified to: $location")
                    }
                })

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking LocationManager - ${e.message}")
        }
    }

    private fun createMockLocation(originLocation: Location? = null, provider: String = LocationManager.GPS_PROVIDER): Location {
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
            XposedBridge.log("$tag invoked hidden API - setIsFromMockProvider: false)")
        } catch (e: Exception) {
            XposedBridge.log("$tag Not possible to mock - ${e.message}")
        }

        return location
    }

    private fun updateLocation() {
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

                XposedBridge.log("$tag Updated fake location values to:")
                XposedBridge.log("\t coordinates: (latitude = $latitude, longitude = $longitude)")
                XposedBridge.log("\t accuracy: $userAccuracy")
                XposedBridge.log("\t altitude: $userAltitude")
            } ?: XposedBridge.log("$tag Last clicked location is null")
        } catch (e: Exception) {
            XposedBridge.log("$tag Error - ${e.message}")
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