package com.noobexon.xposedfakelocation.manager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import org.osmdroid.config.Configuration
import com.noobexon.xposedfakelocation.data.repository.PreferencesRepository
import com.noobexon.xposedfakelocation.manager.ui.navigation.AppNavGraph
import com.noobexon.xposedfakelocation.manager.ui.theme.XposedFakeLocationTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("WorldReadableFiles")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO - test this in fresh install without activating the module first. i think its problematic.
        Configuration.getInstance().load(this, getPreferences(MODE_WORLD_READABLE))
        PreferencesRepository(this.application).clearNonPersistentSettings()
        enableEdgeToEdge()
        setContent {
            XposedFakeLocationTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}

