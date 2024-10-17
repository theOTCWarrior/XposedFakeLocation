package com.noobexon.xposedfakelocation.xposed.location

import android.content.Context

import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class LocationApiHooks(appContext: Context, appLpparam: LoadPackageParam) {
    val tag = "[LocationApiHooks]"

    private val context = appContext
    private val lpparam = appLpparam

    fun initModule() {
        XposedBridge.log("$tag Instantiated Module successfully")
    }
}