package com.noobexon.xposedfakelocation.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.noobexon.xposedfakelocation.data.model.FavoriteLocation
import com.noobexon.xposedfakelocation.data.model.IsPlayingPreference
import com.noobexon.xposedfakelocation.data.model.LastClickedLocation
import com.noobexon.xposedfakelocation.util.SHARED_PREFS_FILE

// Key constants for Gson serialized preferences
private const val KEY_IS_PLAYING_PREF = "is_playing_pref"
private const val KEY_LAST_CLICKED_LOCATION = "last_clicked_location"

class PreferencesRepository(context: Context) {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
    private val gson = Gson()

    // Save and retrieve isPlaying using Gson
    fun saveIsPlaying(isPlaying: Boolean) {
        val isPlayingPref = IsPlayingPreference(isPlaying)
        val json = gson.toJson(isPlayingPref)
        sharedPrefs.edit()
            .putString(KEY_IS_PLAYING_PREF, json)
            .apply()
    }

    fun getIsPlaying(): Boolean {
        val json = sharedPrefs.getString(KEY_IS_PLAYING_PREF, null)
        return if (json != null) {
            val isPlayingPref = gson.fromJson(json, IsPlayingPreference::class.java)
            isPlayingPref.isPlaying
        } else {
            false
        }
    }

    // Save and retrieve last clicked location using Gson
    fun saveLastClickedLocation(latitude: Float, longitude: Float) {
        val location = LastClickedLocation(latitude, longitude)
        val json = gson.toJson(location)
        sharedPrefs.edit()
            .putString(KEY_LAST_CLICKED_LOCATION, json)
            .apply()
    }

    fun getLastClickedLocation(): Pair<Float, Float>? {
        val json = sharedPrefs.getString(KEY_LAST_CLICKED_LOCATION, null)
        return if (json != null) {
            val location = gson.fromJson(json, LastClickedLocation::class.java)
            Pair(location.latitude, location.longitude)
        } else {
            null
        }
    }

    fun clearLastClickedLocation() {
        sharedPrefs.edit()
            .remove(KEY_LAST_CLICKED_LOCATION)
            .apply()
    }

    // Existing methods for handling favorites remain the same
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
