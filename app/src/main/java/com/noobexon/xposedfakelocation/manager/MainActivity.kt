package com.noobexon.xposedfakelocation.manager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.noobexon.xposedfakelocation.data.repository.PreferencesRepository
import com.noobexon.xposedfakelocation.manager.ui.navigation.AppNavGraph
import com.noobexon.xposedfakelocation.manager.ui.theme.XposedFakeLocationTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    @SuppressLint("WorldReadableFiles")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getPreferences(MODE_WORLD_READABLE))
        enableEdgeToEdge()
        resetSettings()
        setContent {
            XposedFakeLocationTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }

    private fun resetSettings() {
        PreferencesRepository(this.application).apply {
            clearLastClickedLocation()
            saveIsPlaying(false)
        }
    }
}

