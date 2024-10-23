package com.noobexon.xposedfakelocation.xposed

import android.app.Application
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.noobexon.xposedfakelocation.data.MANAGER_APP_PACKAGE_NAME
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MainHook : IXposedHookLoadPackage {
    val tag = "[MainHook]"

    lateinit var context: Context

    private var locationApiHooks: LocationApiHooks? = null
    private var systemServicesHooks: SystemServicesHooks? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        // Avoid hooking own app to prevent recursion
        if (lpparam.packageName == MANAGER_APP_PACKAGE_NAME) return

        // If not playing or null, do not proceed with hooking
        if (UserPreferences.getIsPlaying() != true) return

        // Hook system services if user asked for system wide hooks
        if (lpparam.packageName == "android") {
            systemServicesHooks = SystemServicesHooks(lpparam).also { it.initHooks() }
        }

        initHookingLogic(lpparam)
    }

    private fun initHookingLogic(lpparam: LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.app.Instrumentation",
            lpparam.classLoader,
            "callApplicationOnCreate",
            Application::class.java,
            object : XC_MethodHook() {
                @RequiresApi(Build.VERSION_CODES.S)
                override fun afterHookedMethod(param: MethodHookParam) {
                    context = (param.args[0] as Application).applicationContext.also {
                        XposedBridge.log("$tag Target App's context has been acquired successfully.")
                        Toast.makeText(it, "Fake Location Is Active!", Toast.LENGTH_SHORT).show()
                    }
                    locationApiHooks = LocationApiHooks(context, lpparam).also { it.initHooks() }
                }
            }
        )
    }
}