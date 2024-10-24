// LocationApiHooks.kt
package com.noobexon.xposedfakelocation.xposed.hooks

import android.location.Location
import com.noobexon.xposedfakelocation.xposed.utils.LocationUtil
import com.noobexon.xposedfakelocation.xposed.utils.PreferencesUtil
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class LocationApiHooks(val appLpparam: LoadPackageParam) {
    private val tag = "[LocationApiHooks]"

    fun initHooks() {
        hookLocationAPI()
        XposedBridge.log("$tag Instantiated hooks successfully")
    }

    private fun hookLocationAPI() {
        hookLocation(appLpparam.classLoader)
        hookLocationManager(appLpparam.classLoader)
    }

    private fun hookLocation(classLoader: ClassLoader) {
        try {
            val locationClass = XposedHelpers.findClass("android.location.Location", classLoader)

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getLatitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                        XposedBridge.log("$tag Entered method getLatitude()")
                        param.result = LocationUtil.fakeLatitude
                        XposedBridge.log("\t Modified to: ${LocationUtil.fakeLatitude} (original method not executed)")
                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getLongitude",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                        XposedBridge.log("$tag Entered method getLongitude()")
                        param.result =  LocationUtil.fakeLongitude
                        XposedBridge.log("\t Modified to: ${LocationUtil.fakeLongitude} (original method not executed)")

                    }
                })

            XposedHelpers.findAndHookMethod(
                locationClass,
                "getAccuracy",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        LocationUtil.updateLocation()
                        XposedBridge.log("$tag Entered method getAccuracy()")
                        if (PreferencesUtil.getUseAccuracy() == true) {
                            param.result =  LocationUtil.fakeAccuracy
                            XposedBridge.log("\t Modified to: ${LocationUtil.fakeAccuracy} (original method not executed)")
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
                        if (PreferencesUtil.getUseAltitude() == true) {
                            param.result =  LocationUtil.fakeAltitude
                            XposedBridge.log("\t Modified to: ${LocationUtil.fakeAltitude} (original method not executed)")
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
                        val fakeLocation =  LocationUtil.createFakeLocation(param.args[0] as? Location)
                        param.args[0] = fakeLocation
                        XposedBridge.log("\t Modified to: $fakeLocation")
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
                        XposedBridge.log("\t Requested data from: $provider")
                        val fakeLocation =  LocationUtil.createFakeLocation(provider = provider)
                        param.result = fakeLocation
                        XposedBridge.log("\t Modified to: $fakeLocation")
                    }
                })

        } catch (e: Exception) {
            XposedBridge.log("$tag Error hooking LocationManager - ${e.message}")
        }
    }
}