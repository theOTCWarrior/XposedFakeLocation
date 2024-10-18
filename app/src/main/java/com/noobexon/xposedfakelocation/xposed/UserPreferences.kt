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

    fun getIsPlaying(): Boolean {
        preferences.reload() // Ensure we have the latest preferences
        val json = preferences.getString(KEY_IS_PLAYING_PREF, null)
        val isPlaying = if (json != null) {
            val isPlayingPref = Gson().fromJson(json, IsPlayingPreference::class.java)
            isPlayingPref.isPlaying
        } else {
            false
        }
        XposedBridge.log("$tag IsPlaying value: $isPlaying")
        return isPlaying
    }

    fun getLastClickedLocation(): LastClickedLocation? {
        preferences.reload() // Ensure we have the latest preferences
        val json = preferences.getString(KEY_LAST_CLICKED_LOCATION, null)
        val lastClickedLocation = if (json != null) {
            val lastClickedLocationPref = Gson().fromJson(json, LastClickedLocation::class.java)
            LastClickedLocation(lastClickedLocationPref.latitude, lastClickedLocationPref.longitude)
        } else {
            null
        }
        XposedBridge.log("$tag lastClickedLocation value: $lastClickedLocation")
        return lastClickedLocation
    }
}