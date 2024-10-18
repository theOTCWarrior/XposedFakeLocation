package com.noobexon.xposedfakelocation.xposed

import com.google.gson.Gson
import com.noobexon.xposedfakelocation.data.KEY_IS_PLAYING_PREF
import com.noobexon.xposedfakelocation.data.KEY_LAST_CLICKED_LOCATION
import com.noobexon.xposedfakelocation.data.MANAGER_APP_PACKAGE_NAME
import com.noobexon.xposedfakelocation.data.SHARED_PREFS_FILE
import com.noobexon.xposedfakelocation.data.model.IsPlayingPreference
import com.noobexon.xposedfakelocation.data.model.LastClickedLocation
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge

object UserPreferences {
    private const val tag = "[UserPreferences]"

    private val preferences: XSharedPreferences = XSharedPreferences(MANAGER_APP_PACKAGE_NAME, SHARED_PREFS_FILE).apply {
        makeWorldReadable()
        reload()
    }

    fun getIsPlaying(): IsPlayingPreference? {
        return getPreference<IsPlayingPreference>(KEY_IS_PLAYING_PREF)
    }

    fun getLastClickedLocation(): LastClickedLocation? {
        return getPreference<LastClickedLocation>(KEY_LAST_CLICKED_LOCATION)
    }

    private inline fun <reified T> getPreference(key: String): T? {
        preferences.reload() // Ensure we have the latest preferences
        val json = preferences.getString(key, null)
        return if (json != null) {
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