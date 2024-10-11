package com.noobexon.xposedfakelocation.ui.map.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noobexon.xposedfakelocation.ui.map.MapViewModel

@Composable
fun GoToPointDialog(
    mapViewModel: MapViewModel,
    onDismissRequest: () -> Unit,
    onGoToPoint: (latitude: Double, longitude: Double) -> Unit
) {
    val latitudeInput by mapViewModel.latitudeInput
    val longitudeInput by mapViewModel.longitudeInput
    val latitudeError by mapViewModel.latitudeError
    val longitudeError by mapViewModel.longitudeError

    AlertDialog(
        onDismissRequest = {
            mapViewModel.clearGoToPointInputs()
            onDismissRequest()
        },
        title = { Text("Go to Point") },
        text = {
            Column {
                OutlinedTextField(
                    value = latitudeInput,
                    onValueChange = { mapViewModel.updateLatitudeInput(it) },
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
                    onValueChange = { mapViewModel.updateLongitudeInput(it) },
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
                    mapViewModel.validateAndGo { latitude, longitude ->
                        onGoToPoint(latitude, longitude)
                        mapViewModel.clearGoToPointInputs()
                        onDismissRequest()
                    }
                }
            ) {
                Text("Go")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    mapViewModel.clearGoToPointInputs()
                    onDismissRequest()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}