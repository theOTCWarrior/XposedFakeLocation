package com.noobexon.xposedfakelocation.xposed.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import androidx.annotation.RequiresApi
import com.noobexon.xposedfakelocation.data.DEFAULT_ACCURACY
import com.noobexon.xposedfakelocation.data.earthRadius
import com.noobexon.xposedfakelocation.data.pi
import com.noobexon.xposedfakelocation.xposed.UserPreferences
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.util.*
import kotlin.math.cos

class LocationApiHooks(val appContext: Context, val appLpparam: LoadPackageParam) {
    private val tag = "[LocationApiHooks]"

    private val random: Random = Random()

    var latitude: Double = 40.7128
    var longitude: Double = -74.0060
    var accuracy: Double = UserPreferences.getAccuracy() ?: DEFAULT_ACCURACY
    private var lastUpdateTime: Long = 0

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
            val lmServiceClass = XposedHelpers.findClass("com.android.server.LocationManagerService", classLoader)

            // Hook getLastLocation method
            XposedHelpers.findAndHookMethod(
                lmServiceClass,
                "getLastLocation",
                LocationRequest::class.java,
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val location = createMockLocation()
                        param.result = location
                    }
                })

            // Hook other methods to return false
            val methodsToReplace = arrayOf(
                "addGnssBatchingCallback",
                "addGnssMeasurementsListener",
                "addGnssNavigationMessageListener"
            )

            for (methodName in methodsToReplace) {
                XposedHelpers.findAndHookMethod(
                    lmServiceClass,
                    methodName,
                    XC_MethodReplacement.returnConstant(false)
                )
            }

            // Hook Receiver class
            val receiverClass = XposedHelpers.findClass(
                "com.android.server.LocationManagerService\$Receiver",
                classLoader
            )

            XposedHelpers.findAndHookMethod(
                receiverClass,
                "callLocationChangedLocked",
                Location::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        val location = createMockLocation(param.args[0] as? Location)
                        param.args[0] = location
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

            // Hook getLatitude method
            XposedHelpers.findAndHookMethod(
                locationClass,
                "getLatitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        param.result = latitude
                    }
                })

            // Hook getLongitude method
            XposedHelpers.findAndHookMethod(
                locationClass,
                "getLongitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        param.result = longitude
                    }
                })

            // Hook getAccuracy method
            XposedHelpers.findAndHookMethod(
                locationClass,
                "getAccuracy",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        param.result = accuracy.toFloat()
                    }
                })

            // Hook set method
            XposedHelpers.findAndHookMethod(
                locationClass,
                "set",
                Location::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        val location = createMockLocation(param.args[0] as? Location)
                        param.args[0] = location
                    }
                })

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking Location class - ${e.message}")
        }
    }

    private fun hookLocationManager(classLoader: ClassLoader) {
        try {
            val locationManagerClass = XposedHelpers.findClass("android.location.LocationManager", classLoader)

            // Hook getLastKnownLocation method
            XposedHelpers.findAndHookMethod(
                locationManagerClass,
                "getLastKnownLocation",
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        updateLocation()
                        val provider = param.args[0] as String
                        val location = createMockLocation(provider = provider)
                        param.result = location

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
                accuracy = accuracy
                bearing = originLocation.bearing
                bearingAccuracyDegrees = originLocation.bearingAccuracyDegrees
                elapsedRealtimeNanos = originLocation.elapsedRealtimeNanos
                verticalAccuracyMeters = originLocation.verticalAccuracyMeters
            }
        }

        location.latitude = latitude
        location.longitude = longitude
        location.altitude = 0.0
        location.speed = 0F
        location.speedAccuracyMetersPerSecond = 0F

        XposedBridge.log("$tag created mock Location instance with (lat: ${location.latitude}, lng: ${location.longitude})")

        try {
            HiddenApiBypass.invoke(location.javaClass, location, "setIsFromMockProvider", false)
        } catch (e: Exception) {
            XposedBridge.log("$tag Not possible to mock - ${e.message}")
        }

        return location
    }

    private fun updateLocation() {
        try {
            UserPreferences.getLastClickedLocation()?.let {
                lastUpdateTime = System.currentTimeMillis()
                accuracy = UserPreferences.getAccuracy() ?: DEFAULT_ACCURACY

                val x = (random.nextInt(50) -15).toDouble()
                val y = (random.nextInt(50) -15).toDouble()

                val deltaLatitude = x / earthRadius
                val deltaLongitude = y / (earthRadius * cos(pi * it.latitude / 180.0))

                latitude = (if (UserPreferences.getUseRandomize() == true) it.latitude + (deltaLatitude * 180.0 / pi) else it.latitude)
                longitude = (if (UserPreferences.getUseRandomize() == true) it.longitude + (deltaLongitude * 180.0 / pi) else it.longitude)

                XposedBridge.log("$tag Updated fake location to point to (lat = $latitude, lng = $longitude)")

            } ?: XposedBridge.log("$tag Last clicked location is null")
        } catch (e: Exception) {
            XposedBridge.log("$tag Error - ${e.message}")
        }
    }
}