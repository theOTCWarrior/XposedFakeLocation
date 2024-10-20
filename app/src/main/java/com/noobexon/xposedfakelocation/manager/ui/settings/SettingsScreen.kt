//SettingsScreen.kt
package com.noobexon.xposedfakelocation.manager.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel ()
) {
    val useAccuracy by settingsViewModel.useAccuracy.collectAsState()
    val accuracy by settingsViewModel.accuracy.collectAsState()
    var accuracyInput by remember { mutableStateOf(accuracy.toString()) }

    val useAltitude by settingsViewModel.useAltitude.collectAsState()
    val altitude by settingsViewModel.altitude.collectAsState()
    var altitudeInput by remember { mutableStateOf(altitude.toString()) }

    val randomize by settingsViewModel.randomize.collectAsState()

    val focusManager = LocalFocusManager.current

    LaunchedEffect(accuracy) {
        if (accuracy.toString() != accuracyInput) {
            accuracyInput = accuracy.toString()
        }
    }

    LaunchedEffect(altitude) {
        if (altitude.toString() != altitudeInput) {
            altitudeInput = altitude.toString()
        }
    }

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Accuracy Setting
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Accuracy")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = useAccuracy,
                        onCheckedChange = { settingsViewModel.setUseAccuracy(it) }
                    )
                }

                if (useAccuracy) {
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = accuracyInput.toFloatOrNull() == null,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Altitude Setting
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Altitude")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = useAltitude,
                        onCheckedChange = { settingsViewModel.setUseAltitude(it) }
                    )
                }

                if (useAltitude) {
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = altitudeInput.toFloatOrNull() == null,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Randomize Setting
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
}