//SettingsScreen.kt
package com.noobexon.xposedfakelocation.manager.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel ()
) {
    val accuracy by settingsViewModel.accuracy.collectAsState()
    val altitude by settingsViewModel.altitude.collectAsState()
    val randomize by settingsViewModel.randomize.collectAsState()

    var accuracyInput by remember { mutableStateOf(accuracy.toString()) }
    var altitudeInput by remember { mutableStateOf(altitude.toString()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = accuracyInput,
                onValueChange = {
                    accuracyInput = it
                    val value = it.toFloatOrNull()
                    if (value != null) {
                        settingsViewModel.setAccuracy(value)
                    }
                },
                label = { Text("Accuracy") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = accuracyInput.toFloatOrNull() == null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = altitudeInput,
                onValueChange = {
                    altitudeInput = it
                    val value = it.toFloatOrNull()
                    if (value != null) {
                        settingsViewModel.setAltitude(value)
                    }
                },
                label = { Text("Altitude") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = accuracyInput.toFloatOrNull() == null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Randomize")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = randomize,
                    onCheckedChange = { settingsViewModel.setRandomize(it) }
                )
            }
        }
    }
}