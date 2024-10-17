package com.noobexon.xposedfakelocation.manager.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.noobexon.xposedfakelocation.manager.data.model.FavoriteLocation
import com.noobexon.xposedfakelocation.manager.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository = PreferencesRepository(application)

    private val _favorites = MutableStateFlow<List<FavoriteLocation>>(emptyList())
    val favorites: StateFlow<List<FavoriteLocation>> get() = _favorites

    private val _selectedFavorite = MutableStateFlow<FavoriteLocation?>(null)
    val selectedFavorite: StateFlow<FavoriteLocation?> get() = _selectedFavorite

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        _favorites.value = preferencesRepository.getFavorites()
    }

    fun removeFavorite(favorite: FavoriteLocation) {
        preferencesRepository.removeFavorite(favorite)
        loadFavorites() // Refresh the list
    }

    fun selectFavorite(favorite: FavoriteLocation) {
        _selectedFavorite.value = favorite
        // Additional logic to add marker on the map or handle selection can be added here.
    }
}