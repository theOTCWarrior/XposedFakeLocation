// MapScreen.kt
package com.noobexon.xposedfakelocation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapScreen(viewModel: MainViewModel) {
    val isPlaying by viewModel.isPlaying
    val lastClickedLocation by viewModel.lastClickedLocation

    val isFabClickable = lastClickedLocation != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isFabClickable) {
                        viewModel.togglePlaying()
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(16.dp),
                containerColor = if (isFabClickable) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                },
                contentColor = if (isFabClickable) {
                    contentColorFor(MaterialTheme.colorScheme.primary)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = if (isFabClickable) 6.dp else 0.dp,
                    pressedElevation = if (isFabClickable) 12.dp else 0.dp
                )
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Stop" else "Play"
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapViewContainer(viewModel)
        }
    }
}
