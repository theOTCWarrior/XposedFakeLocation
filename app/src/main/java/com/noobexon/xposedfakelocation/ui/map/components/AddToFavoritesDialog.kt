package com.noobexon.xposedfakelocation.ui.map.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp

@Composable
fun AddToFavoritesDialog(
    onDismissRequest: () -> Unit,
    initialLatitude: String = "",
    initialLongitude: String = "",
    onAddFavorite: (name: String, latitude: Double, longitude: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf(initialLatitude) }
    var longitude by remember { mutableStateOf(initialLongitude) }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add to Favorites") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { latitude = it },
                    label = { Text("Latitude") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { longitude = it },
                    label = { Text("Longitude") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Validate inputs
                    val nameText = name.trim()
                    val latText = latitude.trim()
                    val lonText = longitude.trim()
                    if (nameText.isEmpty() || latText.isEmpty() || lonText.isEmpty()) {
                        errorMessage = "Please fill in all fields."
                        return@TextButton
                    }
                    val lat = latText.toDoubleOrNull()
                    val lon = lonText.toDoubleOrNull()
                    if (lat == null || lon == null) {
                        errorMessage = "Invalid coordinates."
                        return@TextButton
                    }
                    if (lat !in -90.0..90.0) {
                        errorMessage = "Latitude must be between -90 and 90."
                        return@TextButton
                    }
                    if (lon !in -180.0..180.0) {
                        errorMessage = "Longitude must be between -180 and 180."
                        return@TextButton
                    }
                    onAddFavorite(nameText, lat, lon)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
