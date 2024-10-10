package com.noobexon.xposedfakelocation.data.repository

import android.content.Context
import com.noobexon.xposedfakelocation.util.KEY_IS_PLAYING
import com.noobexon.xposedfakelocation.util.KEY_LATITUDE
import com.noobexon.xposedfakelocation.util.KEY_LONGITUDE
import com.noobexon.xposedfakelocation.util.SHARED_PREFS_FILE

class PreferencesRepository(context: Context) {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)

    fun saveIsPlaying(isPlaying: Boolean) {
        sharedPrefs.edit().putBoolean(KEY_IS_PLAYING, isPlaying).apply()
    }

    fun getIsPlaying(): Boolean {
        return sharedPrefs.getBoolean(KEY_IS_PLAYING, false)
    }

    fun saveLastClickedLocation(latitude: Float, longitude: Float) {
        sharedPrefs.edit()
            .putFloat(KEY_LATITUDE, latitude)
            .putFloat(KEY_LONGITUDE, longitude)
            .apply()
    }

    fun getLastClickedLocation(): Pair<Float, Float>? {
        val lat = sharedPrefs.getFloat(KEY_LATITUDE, Float.NaN)
        val lon = sharedPrefs.getFloat(KEY_LONGITUDE, Float.NaN)
        return if (!lat.isNaN() && !lon.isNaN()) {
            Pair(lat, lon)
        } else {
            null
        }
    }

    fun clearLastClickedLocation() {
        sharedPrefs.edit()
            .remove(KEY_LATITUDE)
            .remove(KEY_LONGITUDE)
            .apply()
    }
}
