package com.noobexon.xposedfakelocation.xposed

import android.app.Application
import android.content.Context
import com.noobexon.xposedfakelocation.xposed.location.LocationApiHooks
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MainHook : IXposedHookLoadPackage {
    val tag = "[MainHook]"

    lateinit var context: Context
    var locationApiHooks: LocationApiHooks? = null

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        initHookingLogic(lpparam)
    }

    private fun initHookingLogic(lpparam: LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.app.Instrumentation",
            lpparam.classLoader,
            "callApplicationOnCreate",
            Application::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    context = (param.args[0] as Application).applicationContext.also {
                        XposedBridge.log("$tag Target App's context has been acquired successfully.")
                    }
                    locationApiHooks = LocationApiHooks(context, lpparam).also { it.initModule() }
                }
            }
        )
    }
}