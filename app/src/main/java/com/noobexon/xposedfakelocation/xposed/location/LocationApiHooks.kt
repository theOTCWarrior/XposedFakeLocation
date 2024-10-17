package com.noobexon.xposedfakelocation.xposed.location

import android.content.Context

import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class LocationApiHooks(appContext: Context, appLpparam: LoadPackageParam) {
    val tag = "[LocationApiHooks]"

    private val context = appContext
    private val lpparam = appLpparam

    fun initHooks() {
        hookLocationAPI()
        XposedBridge.log("$tag Instantiated hooks successfully")
    }

    private fun hookLocationAPI() {

    }
}