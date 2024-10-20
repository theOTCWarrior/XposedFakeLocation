//SettingsViewModel.kt
package com.noobexon.xposedfakelocation.manager.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.noobexon.xposedfakelocation.data.DEFAULT_ACCURACY
import com.noobexon.xposedfakelocation.data.DEFAULT_ALTITUDE
import com.noobexon.xposedfakelocation.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesRepository = PreferencesRepository(application)

    // StateFlows for the settings
    private val _accuracy = MutableStateFlow(DEFAULT_ACCURACY)
    val accuracy: StateFlow<Float> get() = _accuracy

    private val _altitude = MutableStateFlow(DEFAULT_ALTITUDE)
    val altitude: StateFlow<Float> get() = _altitude

    private val _randomize = MutableStateFlow(false)
    val randomize: StateFlow<Boolean> get() = _randomize

    init {
        // Load settings from preferences
        viewModelScope.launch {
            _accuracy.value = preferencesRepository.getAccuracy()
            _altitude.value = preferencesRepository.getAltitude()
            _randomize.value = preferencesRepository.getRandomize()
        }
    }

    // Functions to update the settings
    fun setAccuracy(value: Float) {
        _accuracy.value = value
        preferencesRepository.saveAccuracy(value)
    }

    fun setAltitude(value: Float) {
        _altitude.value = value
        preferencesRepository.saveAltitude(value)
    }

    fun setRandomize(value: Boolean) {
        _randomize.value = value
        preferencesRepository.saveRandomize(value)
    }
}