// UserPreferences.kt
package com.noobexon.xposedfakelocation.xposed

import com.google.gson.Gson
import com.noobexon.xposedfakelocation.data.DEFAULT_ACCURACY
import com.noobexon.xposedfakelocation.data.DEFAULT_ALTITUDE
import com.noobexon.xposedfakelocation.data.KEY_ACCURACY
import com.noobexon.xposedfakelocation.data.KEY_ALTITUDE
import com.noobexon.xposedfakelocation.data.KEY_IS_PLAYING
import com.noobexon.xposedfakelocation.data.KEY_LAST_CLICKED_LOCATION
import com.noobexon.xposedfakelocation.data.KEY_USE_ACCURACY
import com.noobexon.xposedfakelocation.data.KEY_USE_ALTITUDE
import com.noobexon.xposedfakelocation.data.KEY_USE_RANDOMIZE
import com.noobexon.xposedfakelocation.data.MANAGER_APP_PACKAGE_NAME
import com.noobexon.xposedfakelocation.data.SHARED_PREFS_FILE
import com.noobexon.xposedfakelocation.data.model.LastClickedLocation
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge

object UserPreferences {
    private const val tag = "[UserPreferences]"

    private val preferences: XSharedPreferences = XSharedPreferences(MANAGER_APP_PACKAGE_NAME, SHARED_PREFS_FILE).apply {
        makeWorldReadable()
        reload()
    }

    fun getIsPlaying(): Boolean? {
        return getPreference<Boolean>(KEY_IS_PLAYING)
    }

    fun getLastClickedLocation(): LastClickedLocation? {
        return getPreference<LastClickedLocation>(KEY_LAST_CLICKED_LOCATION)
    }

    fun getUseAccuracy(): Boolean? {
        return getPreference<Boolean>(KEY_USE_ACCURACY)
    }

    fun getAccuracy(): Float? {
        return getPreference<Float>(KEY_ACCURACY)
    }

    fun getUseAltitude(): Boolean? {
        return getPreference<Boolean>(KEY_USE_ALTITUDE)
    }

    fun getAltitude(): Float? {
        return getPreference<Float>(KEY_ALTITUDE)
    }

    fun getUseRandomize(): Boolean? {
        return getPreference<Boolean>(KEY_USE_RANDOMIZE)
    }

    private inline fun <reified T> getPreference(key: String): T? {
        preferences.reload()
        return when (T::class) {
            Float::class -> {
                val defaultValue = when (key) {
                    KEY_ACCURACY -> DEFAULT_ACCURACY
                    KEY_ALTITUDE -> DEFAULT_ALTITUDE
                    else -> -1f
                }
                preferences.getFloat(key, defaultValue) as? T
            }
            Boolean::class -> preferences.getBoolean(key, false) as? T
            else -> {
                val json = preferences.getString(key, null)
                if (json != null) {
                    try {
                        Gson().fromJson(json, T::class.java).also {
                            XposedBridge.log("$tag Retrieved $key: $it")
                        }
                    } catch (e: Exception) {
                        XposedBridge.log("$tag Error parsing $key JSON: ${e.message}")
                        null
                    }
                } else {
                    XposedBridge.log("$tag $key not found in preferences.")
                    null
                }
            }
        }
    }
}