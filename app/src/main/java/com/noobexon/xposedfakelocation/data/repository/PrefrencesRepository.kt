// PreferencesRepository.kt
package com.noobexon.xposedfakelocation.data.repository

import android.annotation.SuppressLint
import android.content.Context
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
import com.noobexon.xposedfakelocation.data.model.FavoriteLocation
import com.noobexon.xposedfakelocation.data.model.IsPlayingPreference
import com.noobexon.xposedfakelocation.data.model.LastClickedLocation

class PreferencesRepository(context: Context) {
    @SuppressLint("WorldReadableFiles")
    private val sharedPrefs = try {
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_WORLD_READABLE)
    } catch (e: SecurityException) {
        // Fallback to MODE_PRIVATE
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
    }
    private val gson = Gson()

    // Is Playing
    fun saveIsPlaying(isPlaying: Boolean) {
        val isPlayingPref = IsPlayingPreference(isPlaying)
        val json = gson.toJson(isPlayingPref)
        sharedPrefs.edit()
            .putString(KEY_IS_PLAYING_PREF, json)
            .apply()
    }

    // Last Clicked Location
    fun saveLastClickedLocation(latitude: Float, longitude: Float) {
        val location = LastClickedLocation(latitude, longitude)
        val json = gson.toJson(location)
        sharedPrefs.edit()
            .putString(KEY_LAST_CLICKED_LOCATION, json)
            .apply()
    }

    fun clearLastClickedLocation() {
        sharedPrefs.edit()
            .remove(KEY_LAST_CLICKED_LOCATION)
            .apply()
    }

    // Settings
    fun saveAccuracy(accuracy: Float) {
        sharedPrefs.edit()
            .putFloat(KEY_ACCURACY, accuracy)
            .apply()
    }

    fun getAccuracy(): Float {
        return sharedPrefs.getFloat(KEY_ACCURACY, DEFAULT_ACCURACY)
    }

    fun saveAltitude(altitude: Float) {
        sharedPrefs.edit()
            .putFloat(KEY_ALTITUDE, altitude)
            .apply()
    }

    fun getAltitude(): Float {
        return sharedPrefs.getFloat(KEY_ALTITUDE, DEFAULT_ALTITUDE)
    }

    fun saveRandomize(randomize: Boolean) {
        sharedPrefs.edit()
            .putBoolean(KEY_RANDOMIZE, randomize)
            .apply()
    }

    fun getRandomize(): Boolean {
        return sharedPrefs.getBoolean(KEY_RANDOMIZE, false)
    }

    // Favorites
    fun addFavorite(favorite: FavoriteLocation) {
        val favorites = getFavorites().toMutableList()
        favorites.add(favorite)
        saveFavorites(favorites)
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
    }

    fun removeFavorite(favorite: FavoriteLocation) {
        val favorites = getFavorites().toMutableList()
        favorites.remove(favorite)
        saveFavorites(favorites)
    }



}
