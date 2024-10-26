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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


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

    val useRandomize by settingsViewModel.useRandomize.collectAsState()
    val randomizeRadius by settingsViewModel.randomizeRadius.collectAsState()
    var randomizeRadiusInput by remember { mutableStateOf(randomizeRadius.toString()) }

    val useVerticalAccuracy by settingsViewModel.useVerticalAccuracy.collectAsState()
    val verticalAccuracy by settingsViewModel.verticalAccuracy.collectAsState()
    var verticalAccuracyInput by remember { mutableStateOf(verticalAccuracy.toString()) }

    val useMeanSeaLevel by settingsViewModel.useMeanSeaLevel.collectAsState()
    val meanSeaLevel by settingsViewModel.meanSeaLevel.collectAsState()
    var meanSeaLevelInput by remember { mutableStateOf(meanSeaLevel.toString()) }

    val useMeanSeaLevelAccuracy by settingsViewModel.useMeanSeaLevelAccuracy.collectAsState()
    val meanSeaLevelAccuracy by settingsViewModel.meanSeaLevelAccuracy.collectAsState()
    var meanSeaLevelAccuracyInput by remember { mutableStateOf(meanSeaLevelAccuracy.toString()) }

    val useSpeed by settingsViewModel.useSpeed.collectAsState()
    val speed by settingsViewModel.speed.collectAsState()
    var speedInput by remember { mutableStateOf(speed.toString()) }

    val useSpeedAccuracy by settingsViewModel.useSpeedAccuracy.collectAsState()
    val speedAccuracy by settingsViewModel.speedAccuracy.collectAsState()
    var speedAccuracyInput by remember { mutableStateOf(speedAccuracy.toString()) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

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

    LaunchedEffect(randomizeRadius) {
        if (randomizeRadius.toString() != randomizeRadiusInput) {
            randomizeRadiusInput = randomizeRadius.toString()
        }
    }

    LaunchedEffect(verticalAccuracy) {
        if (verticalAccuracy.toString() != verticalAccuracyInput) {
            verticalAccuracyInput = verticalAccuracy.toString()
        }
    }

    LaunchedEffect(meanSeaLevel) {
        if (meanSeaLevel.toString() != meanSeaLevelInput) {
            meanSeaLevelInput = meanSeaLevel.toString()
        }
    }

    LaunchedEffect(meanSeaLevelAccuracy) {
        if (meanSeaLevelAccuracy.toString() != meanSeaLevelAccuracyInput) {
            meanSeaLevelAccuracyInput = meanSeaLevelAccuracy.toString()
        }
    }

    LaunchedEffect(speed) {
        if (speed.toString() != speedInput) {
            speedInput = speed.toString()
        }
    }

    LaunchedEffect(speedAccuracy) {
        if (speedAccuracy.toString() != speedAccuracyInput) {
            speedAccuracyInput = speedAccuracy.toString()
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
                    .verticalScroll(scrollState)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Custom Horizontal Accuracy")
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
                            val value = it.toDoubleOrNull()
                            if (value != null) {
                                settingsViewModel.setAccuracy(value)
                            }
                        },
                        label = { Text("Horizontal Accuracy (m)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = accuracyInput.toDoubleOrNull() == null,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Custom Vertical Accuracy")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = useVerticalAccuracy,
                        onCheckedChange = { settingsViewModel.setUseVerticalAccuracy(it) }
                    )
                }

                if (useVerticalAccuracy) {
                    OutlinedTextField(
                        value = verticalAccuracyInput,
                        onValueChange = {
                            verticalAccuracyInput = it
                            val value = it.toFloatOrNull()
                            if (value != null) {
                                settingsViewModel.setVerticalAccuracy(value)
                            }
                        },
                        label = { Text("Vertical Accuracy (m)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = verticalAccuracyInput.toFloatOrNull() == null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Custom Altitude")
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
                            val value = it.toDoubleOrNull()
                            if (value != null) {
                                settingsViewModel.setAltitude(value)
                            }
                        },
                        label = { Text("Altitude (m)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = altitudeInput.toDoubleOrNull() == null,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Randomize Nearby Location")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = useRandomize,
                        onCheckedChange = { settingsViewModel.setUseRandomize(it) }
                    )
                }

                if (useRandomize) {
                    OutlinedTextField(
                        value = randomizeRadiusInput,
                        onValueChange = {
                            randomizeRadiusInput = it
                            val value = it.toDoubleOrNull()
                            if (value != null) {
                                settingsViewModel.setRandomizeRadius(value)
                            }
                        },
                        label = { Text("Randomization Radius (m)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = randomizeRadiusInput.toDoubleOrNull() == null,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Custom Mean Sea Level")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = useMeanSeaLevel,
                        onCheckedChange = { settingsViewModel.setUseMeanSeaLevel(it) }
                    )
                }

                if (useMeanSeaLevel) {
                    OutlinedTextField(
                        value = meanSeaLevelInput,
                        onValueChange = {
                            meanSeaLevelInput = it
                            val value = it.toDoubleOrNull()
                            if (value != null) {
                                settingsViewModel.setMeanSeaLevel(value)
                            }
                        },
                        label = { Text("Mean Sea Level (m)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = meanSeaLevelInput.toDoubleOrNull() == null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Mean Sea Level Accuracy")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = useMeanSeaLevelAccuracy,
                        onCheckedChange = { settingsViewModel.setUseMeanSeaLevelAccuracy(it) }
                    )
                }

                if (useMeanSeaLevelAccuracy) {
                    OutlinedTextField(
                        value = meanSeaLevelAccuracyInput,
                        onValueChange = {
                            meanSeaLevelAccuracyInput = it
                            val value = it.toFloatOrNull()
                            if (value != null) {
                                settingsViewModel.setMeanSeaLevelAccuracy(value)
                            }
                        },
                        label = { Text("Mean Sea Level Accuracy (m)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = meanSeaLevelAccuracyInput.toFloatOrNull() == null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Custom Speed")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = useSpeed,
                        onCheckedChange = { settingsViewModel.setUseSpeed(it) }
                    )
                }

                if (useSpeed) {
                    OutlinedTextField(
                        value = speedInput,
                        onValueChange = {
                            speedInput = it
                            val value = it.toFloatOrNull()
                            if (value != null) {
                                settingsViewModel.setSpeed(value)
                            }
                        },
                        label = { Text("Speed (m/s)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = speedInput.toFloatOrNull() == null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Speed Accuracy")
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = useSpeedAccuracy,
                        onCheckedChange = { settingsViewModel.setUseSpeedAccuracy(it) }
                    )
                }

                if (useSpeedAccuracy) {
                    OutlinedTextField(
                        value = speedAccuracyInput,
                        onValueChange = {
                            speedAccuracyInput = it
                            val value = it.toFloatOrNull()
                            if (value != null) {
                                settingsViewModel.setSpeedAccuracy(value)
                            }
                        },
                        label = { Text("Speed Accuracy (m/s)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        isError = speedAccuracyInput.toFloatOrNull() == null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}