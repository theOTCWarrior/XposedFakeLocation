package com.noobexon.xposedfakelocation.xposed

import android.content.Context
import android.location.Location
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class LocationApiHooks(val appContext: Context, val appLpparam: LoadPackageParam) {
    private val tag = "[LocationApiHooks]"

    fun initHooks() {
        hookLocationAPI()
        XposedBridge.log("$tag Instantiated hooks successfully")
    }

    private fun hookLocationAPI() {
        hookLocationClass(appLpparam.classLoader)
        hookLocationManager(appLpparam.classLoader)
    }

    private fun hookLocationClass(classLoader: ClassLoader) {
        try {
            val locationClass = XposedHelpers.findClass("android.location.Location", classLoader)

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getLatitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                        XposedBridge.log("$tag Entered method getLatitude()")
                        param.result = LocationUtil.latitude
                        XposedBridge.log("\t Modified to: ${LocationUtil.latitude} (original method not executed)")
                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getLongitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                        XposedBridge.log("$tag Entered method getLongitude()")
                        param.result =  LocationUtil.longitude
                        XposedBridge.log("\t Modified to: ${LocationUtil.longitude} (original method not executed)")

                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getAccuracy",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                        XposedBridge.log("$tag Entered method getAccuracy()")
                        XposedBridge.log("\t Should modify: ${UserPreferences.getUseAccuracy()}")
                        if ( LocationUtil.userAccuracy != null) {
                            param.result =  LocationUtil.userAccuracy!!
                            XposedBridge.log("\t Modified to: ${LocationUtil.userAccuracy} (original method not executed)")
                        }
                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getAltitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                        XposedBridge.log("$tag Entered method getAltitude()")
                        XposedBridge.log("\t Should modify: ${UserPreferences.getUseAltitude()}")
                        if ( LocationUtil.userAltitude != null) {
                            param.result =  LocationUtil.userAltitude!!
                            XposedBridge.log("\t Modified to: ${LocationUtil.userAltitude} (original method not executed)")
                        }
                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "set",
                Location::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                        XposedBridge.log("$tag Entered method set(location)")
                        val location =  LocationUtil.createMockLocation(param.args[0] as? Location)
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
                        LocationUtil.updateLocation()
                        XposedBridge.log("$tag Entered method getLastKnownLocation(provider)")
                        val provider = param.args[0] as String
                        XposedBridge.log("\t Requested provider: $provider")
                        val location =  LocationUtil.createMockLocation(provider = provider)
                        param.result = location
                        XposedBridge.log("\t Modified to: $location")
                    }
                })

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking LocationManager - ${e.message}")
        }
    }
}