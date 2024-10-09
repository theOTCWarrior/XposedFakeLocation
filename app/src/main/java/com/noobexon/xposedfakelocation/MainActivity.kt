package com.noobexon.xposedfakelocation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.noobexon.xposedfakelocation.ui.theme.XposedFakeLocationTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE))
        enableEdgeToEdge()
        ContextHolder.appContext = applicationContext
        setContent {
            XposedFakeLocationTheme {
                AppContent()
            }
        }
    }
}
