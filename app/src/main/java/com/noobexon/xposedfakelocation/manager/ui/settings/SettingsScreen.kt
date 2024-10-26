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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel ()
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val settings = listOf(
        // Randomize Nearby Location
        SettingData(
            title = "Randomize Nearby Location",
            useValueState = settingsViewModel.useRandomize.collectAsState(),
            valueState = settingsViewModel.randomizeRadius.collectAsState(),
            setUseValue = settingsViewModel::setUseRandomize,
            setValue = settingsViewModel::setRandomizeRadius,
            label = "Randomization Radius (m)",
            isDouble = true
        ),
        // Custom Horizontal Accuracy
        SettingData(
            title = "Custom Horizontal Accuracy",
            useValueState = settingsViewModel.useAccuracy.collectAsState(),
            valueState = settingsViewModel.accuracy.collectAsState(),
            setUseValue = settingsViewModel::setUseAccuracy,
            setValue = settingsViewModel::setAccuracy,
            label = "Horizontal Accuracy (m)",
            isDouble = true
        ),
        // Custom Vertical Accuracy
        SettingData(
            title = "Custom Vertical Accuracy",
            useValueState = settingsViewModel.useVerticalAccuracy.collectAsState(),
            valueState = settingsViewModel.verticalAccuracy.collectAsState(),
            setUseValue = settingsViewModel::setUseVerticalAccuracy,
            setValue = settingsViewModel::setVerticalAccuracy,
            label = "Vertical Accuracy (m)",
            isDouble = false
        ),
        // Custom Altitude
        SettingData(
            title = "Custom Altitude",
            useValueState = settingsViewModel.useAltitude.collectAsState(),
            valueState = settingsViewModel.altitude.collectAsState(),
            setUseValue = settingsViewModel::setUseAltitude,
            setValue = settingsViewModel::setAltitude,
            label = "Altitude (m)",
            isDouble = true
        ),
        // Custom MSL
        SettingData(
            title = "Custom MSL",
            useValueState = settingsViewModel.useMeanSeaLevel.collectAsState(),
            valueState = settingsViewModel.meanSeaLevel.collectAsState(),
            setUseValue = settingsViewModel::setUseMeanSeaLevel,
            setValue = settingsViewModel::setMeanSeaLevel,
            label = "MSL (m)",
            isDouble = true
        ),
        // Custom MSL Accuracy
        SettingData(
            title = "Custom MSL Accuracy",
            useValueState = settingsViewModel.useMeanSeaLevelAccuracy.collectAsState(),
            valueState = settingsViewModel.meanSeaLevelAccuracy.collectAsState(),
            setUseValue = settingsViewModel::setUseMeanSeaLevelAccuracy,
            setValue = settingsViewModel::setMeanSeaLevelAccuracy,
            label = "MSL Accuracy (m)",
            isDouble = false
        ),
        // Custom Speed
        SettingData(
            title = "Custom Speed",
            useValueState = settingsViewModel.useSpeed.collectAsState(),
            valueState = settingsViewModel.speed.collectAsState(),
            setUseValue = settingsViewModel::setUseSpeed,
            setValue = settingsViewModel::setSpeed,
            label = "Speed (m/s)",
            isDouble = false
        ),
        // Custom Speed Accuracy
        SettingData(
            title = "Custom Speed Accuracy",
            useValueState = settingsViewModel.useSpeedAccuracy.collectAsState(),
            valueState = settingsViewModel.speedAccuracy.collectAsState(),
            setUseValue = settingsViewModel::setUseSpeedAccuracy,
            setValue = settingsViewModel::setSpeedAccuracy,
            label = "Speed Accuracy (m/s)",
            isDouble = false
        )
    )

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
                ) { focusManager.clearFocus() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                settings.forEach { setting ->
                    if (setting.isDouble) {
                        DoubleSettingComposable(
                            setting = setting as SettingData<Double>,
                            focusManager = focusManager
                        )
                    } else {
                        FloatSettingComposable(
                            setting = setting as SettingData<Float>,
                            focusManager = focusManager
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DoubleSettingItem(
    title: String,
    useValue: Boolean,
    onUseValueChange: (Boolean) -> Unit,
    onValueChange: (Double) -> Unit,
    valueInput: String,
    onValueInputChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    focusManager: FocusManager
) {
    SettingItem(
        title = title,
        useValue = useValue,
        onUseValueChange = onUseValueChange,
        valueInput = valueInput,
        onValueInputChange = onValueInputChange,
        label = label,
        isError = isError,
        parseValue = String::toDoubleOrNull,
        onParsedValueChange = onValueChange,
        focusManager = focusManager
    )
}

@Composable
fun FloatSettingItem(
    title: String,
    useValue: Boolean,
    onUseValueChange: (Boolean) -> Unit,
    onValueChange: (Float) -> Unit,
    valueInput: String,
    onValueInputChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    focusManager: FocusManager
) {
    SettingItem(
        title = title,
        useValue = useValue,
        onUseValueChange = onUseValueChange,
        valueInput = valueInput,
        onValueInputChange = onValueInputChange,
        label = label,
        isError = isError,
        parseValue = String::toFloatOrNull,
        onParsedValueChange = onValueChange,
        focusManager = focusManager
    )
}

@Composable
private fun <T> SettingItem(
    title: String,
    useValue: Boolean,
    onUseValueChange: (Boolean) -> Unit,
    valueInput: String,
    onValueInputChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    parseValue: (String) -> T?,
    onParsedValueChange: (T) -> Unit,
    focusManager: FocusManager
) {
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(title)
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = useValue,
            onCheckedChange = onUseValueChange
        )
    }

    if (useValue) {
        OutlinedTextField(
            value = valueInput,
            onValueChange = {
                onValueInputChange(it)
                val parsedValue = parseValue(it)
                if (parsedValue != null) {
                    onParsedValueChange(parsedValue)
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            isError = isError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

data class SettingData<T>(
    val title: String,
    val useValueState: State<Boolean>,
    val valueState: State<T>,
    val setUseValue: (Boolean) -> Unit,
    val setValue: (T) -> Unit,
    val label: String,
    val isDouble: Boolean
)

@Composable
fun DoubleSettingComposable(
    setting: SettingData<Double>,
    focusManager: FocusManager
) {
    var valueInput by remember { mutableStateOf(setting.valueState.value.toString()) }
    val isError = valueInput.toDoubleOrNull() == null

    LaunchedEffect(setting.valueState.value) {
        if (setting.valueState.value.toString() != valueInput) {
            valueInput = setting.valueState.value.toString()
        }
    }

    DoubleSettingItem(
        title = setting.title,
        useValue = setting.useValueState.value,
        onUseValueChange = setting.setUseValue,
        onValueChange = setting.setValue,
        valueInput = valueInput,
        onValueInputChange = { valueInput = it },
        label = setting.label,
        isError = isError,
        focusManager = focusManager
    )
}

@Composable
fun FloatSettingComposable(
    setting: SettingData<Float>,
    focusManager: FocusManager
) {
    var valueInput by remember { mutableStateOf(setting.valueState.value.toString()) }
    val isError = valueInput.toFloatOrNull() == null

    LaunchedEffect(setting.valueState.value) {
        if (setting.valueState.value.toString() != valueInput) {
            valueInput = setting.valueState.value.toString()
        }
    }

    FloatSettingItem(
        title = setting.title,
        useValue = setting.useValueState.value,
        onUseValueChange = setting.setUseValue,
        onValueChange = setting.setValue,
        valueInput = valueInput,
        onValueInputChange = { valueInput = it },
        label = setting.label,
        isError = isError,
        focusManager = focusManager
    )
}