// PreferencesRepository.kt
package com.noobexon.xposedfakelocation.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noobexon.xposedfakelocation.data.SHARED_PREFS_FILE
import com.noobexon.xposedfakelocation.data.KEY_ACCURACY
import com.noobexon.xposedfakelocation.data.KEY_ALTITUDE
import com.noobexon.xposedfakelocation.data.KEY_IS_PLAYING_PREF
import com.noobexon.xposedfakelocation.data.KEY_LAST_CLICKED_LOCATION
import com.noobexon.xposedfakelocation.data.KEY_RANDOMIZE
import com.noobexon.xposedfakelocation.data.DEFAULT_ACCURACY
import com.noobexon.xposedfakelocation.data.DEFAULT_ALTITUDE
import com.noobexon.xposedfakelocation.data.DEFAULT_USE_ACCURACY
import com.noobexon.xposedfakelocation.data.DEFAULT_USE_ALTITUDE
import com.noobexon.xposedfakelocation.data.KEY_USE_ACCURACY
import com.noobexon.xposedfakelocation.data.KEY_USE_ALTITUDE
import com.noobexon.xposedfakelocation.data.model.FavoriteLocation
import com.noobexon.xposedfakelocation.data.model.IsPlayingPreference
import com.noobexon.xposedfakelocation.data.model.LastClickedLocation
import java.io.File

class PreferencesRepository(context: Context) {

    // 'find /data -name "xposed_shared_prefs.xml"' to find location of shared preferences in fs.

    private val tag = "PreferencesRepository"

    @SuppressLint("WorldReadableFiles")
    private val sharedPrefs = try {
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_WORLD_READABLE)
    } catch (e: SecurityException) {
        // Fallback to MODE_PRIVATE
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
    }
    private val gson = Gson()

    // Get the path to the shared preferences file
    private val prefsFile: File = File(context.filesDir.parentFile, "shared_prefs/$SHARED_PREFS_FILE.xml")

    // Is Playing
    fun saveIsPlaying(isPlaying: Boolean) {
        val isPlayingPref = IsPlayingPreference(isPlaying)
        val json = gson.toJson(isPlayingPref)
        sharedPrefs.edit()
            .putString(KEY_IS_PLAYING_PREF, json)
            .apply()
        Log.d(tag, "Saved IsPlaying: $json to ${prefsFile.absolutePath}")
    }

    // Last Clicked Location
    fun saveLastClickedLocation(latitude: Float, longitude: Float) {
        val location = LastClickedLocation(latitude, longitude)
        val json = gson.toJson(location)
        sharedPrefs.edit()
            .putString(KEY_LAST_CLICKED_LOCATION, json)
            .apply()
        Log.d(tag, "Saved LastClickedLocation: $json to ${prefsFile.absolutePath}")
    }

    fun clearLastClickedLocation() {
        sharedPrefs.edit()
            .remove(KEY_LAST_CLICKED_LOCATION)
            .apply()
        Log.d(tag, "Cleared LastClickedLocation from ${prefsFile.absolutePath}")
    }

    // Settings
    fun saveUseAccuracy(useAccuracy: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_ACCURACY, useAccuracy)
            .apply()
        Log.d(tag, "Saved UseAccuracy: $useAccuracy to ${prefsFile.absolutePath}")
    }

    fun getUseAccuracy(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_ACCURACY, DEFAULT_USE_ACCURACY)
    }

    fun saveAccuracy(accuracy: Float) {
        sharedPrefs.edit()
            .putFloat(KEY_ACCURACY, accuracy)
            .apply()
        Log.d(tag, "Saved Accuracy: $accuracy to ${prefsFile.absolutePath}")
    }

    fun getAccuracy(): Float {
        return sharedPrefs.getFloat(KEY_ACCURACY, DEFAULT_ACCURACY)
    }

    fun saveUseAltitude(useAltitude: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_USE_ALTITUDE, useAltitude)
            .apply()
        Log.d(tag, "Saved UseAltitude: $useAltitude to ${prefsFile.absolutePath}")
    }

    fun getUseAltitude(): Boolean {
        return sharedPrefs.getBoolean(KEY_USE_ALTITUDE, DEFAULT_USE_ALTITUDE)
    }

    fun saveAltitude(altitude: Float) {
        sharedPrefs.edit()
            .putFloat(KEY_ALTITUDE, altitude)
            .apply()
        Log.d(tag, "Saved Altitude: $altitude to ${prefsFile.absolutePath}")
    }

    fun getAltitude(): Float {
        return sharedPrefs.getFloat(KEY_ALTITUDE, DEFAULT_ALTITUDE)
    }

    fun saveRandomize(randomize: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_RANDOMIZE, randomize)
            .apply()
        Log.d(tag, "Saved Randomize: $randomize to ${prefsFile.absolutePath}")
    }

    fun getRandomize(): Boolean {
        return sharedPrefs.getBoolean(KEY_RANDOMIZE, false)
    }

    // Favorites
    fun addFavorite(favorite: FavoriteLocation) {
        val favorites = getFavorites().toMutableList()
        favorites.add(favorite)
        saveFavorites(favorites)
        Log.d(tag, "Added Favorite: $favorite to ${prefsFile.absolutePath}")
    }

    fun getFavorites(): List<FavoriteLocation> {
        val json = sharedPrefs.getString("favorites", null)
        return if (json != null) {
            val type = object : TypeToken<List<FavoriteLocation>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun saveFavorites(favorites: List<FavoriteLocation>) {
        val json = gson.toJson(favorites)
        sharedPrefs.edit()
            .putString("favorites", json)
            .apply()
        Log.d(tag, "Saved Favorites: $json to ${prefsFile.absolutePath}")
    }

    fun removeFavorite(favorite: FavoriteLocation) {
        val favorites = getFavorites().toMutableList()
        favorites.remove(favorite)
        saveFavorites(favorites)
        Log.d(tag, "Removed Favorite: $favorite from ${prefsFile.absolutePath}")
    }
}