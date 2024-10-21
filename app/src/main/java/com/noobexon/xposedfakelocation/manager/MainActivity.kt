package com.noobexon.xposedfakelocation.manager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.noobexon.xposedfakelocation.data.repository.PreferencesRepository
import com.noobexon.xposedfakelocation.manager.ui.navigation.AppNavGraph
import com.noobexon.xposedfakelocation.manager.ui.theme.XposedFakeLocationTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    @SuppressLint("WorldReadableFiles")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var loadSuccess = true

        try {
            Configuration.getInstance().load(this, getPreferences(MODE_WORLD_READABLE))
        } catch (e: SecurityException) {
            loadSuccess = false
        }

        if (loadSuccess) {
            PreferencesRepository(this.application).clearNonPersistentSettings()
            enableEdgeToEdge()
            setContent {
                XposedFakeLocationTheme {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController)
                }
            }
        } else {
            setContent {
                XposedFakeLocationTheme {
                    ErrorScreen()
                }
            }
        }
    }
}

@Composable
fun ErrorScreen() {
    var showDialog by remember { mutableStateOf(true) }
    val context = LocalContext.current

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Module Not Active") },
            text = {
                Text( "XposedFakeLocation module is not active in your Xposed manager app. Please enable it to continue." )
            },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    (context as? ComponentActivity)?.finish()
                }) {
                    Text("OK")
                }
            },
            dismissButton = null
        )
    }
}