package com.noobexon.xposedfakelocation.xposed.location

import android.content.Context
import com.noobexon.xposedfakelocation.xposed.UserPreferences


import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class LocationApiHooks(appContext: Context, appLpparam: LoadPackageParam) {
    val tag = "[LocationApiHooks]"

    fun initHooks() {
        hookLocationAPI()
        UserPreferences.getLastClickedLocation()
        XposedBridge.log("$tag Instantiated hooks successfully")
    }

    private fun hookLocationAPI() {

    }
}