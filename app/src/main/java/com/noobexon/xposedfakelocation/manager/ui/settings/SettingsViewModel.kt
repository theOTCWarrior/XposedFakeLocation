//SettingsViewModel.kt
package com.noobexon.xposedfakelocation.manager.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.noobexon.xposedfakelocation.data.DEFAULT_ACCURACY
import com.noobexon.xposedfakelocation.data.DEFAULT_ALTITUDE
import com.noobexon.xposedfakelocation.data.DEFAULT_RANDOMIZE_RADIUS
import com.noobexon.xposedfakelocation.data.DEFAULT_USE_ACCURACY
import com.noobexon.xposedfakelocation.data.DEFAULT_USE_ALTITUDE
import com.noobexon.xposedfakelocation.data.DEFAULT_USE_RANDOMIZE
import com.noobexon.xposedfakelocation.data.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesRepository = PreferencesRepository(application)

    private val _useAccuracy = MutableStateFlow(DEFAULT_USE_ACCURACY)
    val useAccuracy: StateFlow<Boolean> get() = _useAccuracy

    private val _accuracy = MutableStateFlow(DEFAULT_ACCURACY)
    val accuracy: StateFlow<Double> get() = _accuracy

    private val _useAltitude = MutableStateFlow(DEFAULT_USE_ALTITUDE)
    val useAltitude: StateFlow<Boolean> get() = _useAltitude

    private val _altitude = MutableStateFlow(DEFAULT_ALTITUDE)
    val altitude: StateFlow<Double> get() = _altitude

    private val _useRandomize = MutableStateFlow(DEFAULT_USE_RANDOMIZE)
    val useRandomize: StateFlow<Boolean> get() = _useRandomize

    private val _randomizeRadius = MutableStateFlow(DEFAULT_RANDOMIZE_RADIUS)
    val randomizeRadius: StateFlow<Double> get() = _randomizeRadius

    init {
        viewModelScope.launch {
            _useAccuracy.value = preferencesRepository.getUseAccuracy()
            _accuracy.value = preferencesRepository.getAccuracy()
            _useAltitude.value = preferencesRepository.getUseAltitude()
            _altitude.value = preferencesRepository.getAltitude()
            _useRandomize.value = preferencesRepository.getUseRandomize()
            _randomizeRadius.value = preferencesRepository.getRandomizeRadius()
        }
    }

    fun setUseAccuracy(value: Boolean) {
        _useAccuracy.value = value
        preferencesRepository.saveUseAccuracy(value)
    }

    fun setAccuracy(value: Double) {
        _accuracy.value = value
        preferencesRepository.saveAccuracy(value)
    }

    fun setUseAltitude(value: Boolean) {
        _useAltitude.value = value
        preferencesRepository.saveUseAltitude(value)
    }

    fun setAltitude(value: Double) {
        _altitude.value = value
        preferencesRepository.saveAltitude(value)
    }

    fun setRandomize(value: Boolean) {
        _useRandomize.value = value
        preferencesRepository.saveUseRandomize(value)
    }

    fun setRandomizeRadius(value: Double) {
        _randomizeRadius.value = value
        preferencesRepository.saveRandomizeRadius(value)
    }
}