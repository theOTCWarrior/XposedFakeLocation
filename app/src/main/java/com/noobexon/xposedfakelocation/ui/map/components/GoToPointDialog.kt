package com.noobexon.xposedfakelocation.ui.map.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GoToPointDialog(
    onDismissRequest: () -> Unit,
    onGoToPoint: (latitude: Double, longitude: Double) -> Unit
) {
    var latitudeInput by remember { mutableStateOf("") }
    var longitudeInput by remember { mutableStateOf("") }
    var latitudeError by remember { mutableStateOf<String?>(null) }
    var longitudeError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Go to Point") },
        text = {
            Column {
                OutlinedTextField(
                    value = latitudeInput,
                    onValueChange = { latitudeInput = it },
                    label = { Text("Latitude") },
                    isError = latitudeError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (latitudeError != null) {
                    Text(
                        text = latitudeError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = longitudeInput,
                    onValueChange = { longitudeInput = it },
                    label = { Text("Longitude") },
                    isError = longitudeError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (longitudeError != null) {
                    Text(
                        text = longitudeError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Validate inputs
                    val latitude = latitudeInput.toDoubleOrNull()
                    val longitude = longitudeInput.toDoubleOrNull()
                    var isValid = true

                    if (latitude == null || latitude !in -90.0..90.0) {
                        latitudeError = "Latitude must be between -90 and 90"
                        isValid = false
                    } else {
                        latitudeError = null
                    }

                    if (longitude == null || longitude !in -180.0..180.0) {
                        longitudeError = "Longitude must be between -180 and 180"
                        isValid = false
                    } else {
                        longitudeError = null
                    }

                    if (isValid) {
                        onGoToPoint(latitude!!, longitude!!)
                        onDismissRequest()
                    }
                }
            ) {
                Text("Go")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
