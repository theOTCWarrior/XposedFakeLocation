package com.noobexon.xposedfakelocation.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.noobexon.xposedfakelocation.DrawerContent
import com.noobexon.xposedfakelocation.MainViewModel
import com.noobexon.xposedfakelocation.MapViewContainer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MainViewModel) {
    val isPlaying by viewModel.isPlaying
    val isFabClickable by remember { derivedStateOf { viewModel.isFabClickable } }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val showSettings by viewModel.showSettings
    val showAbout by viewModel.showAbout

    val context = LocalContext.current
    val activity = context as? Activity

    // BackHandler for managing navigation and drawer state
    BackHandler(enabled = showSettings || showAbout || drawerState.isOpen) {
        when {
            drawerState.isOpen -> {
                scope.launch { drawerState.close() }
            }
            showSettings -> {
                viewModel.toggleSettings()
            }
            showAbout -> {
                viewModel.toggleAbout()
            }
            else -> {
                activity?.finish()
            }
        }
    }

    // Scaffold with drawer
    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                onSettingsClick = {
                    scope.launch { drawerState.close() }
                    viewModel.toggleSettings()
                },
                onAboutClick = {
                    scope.launch { drawerState.close() }
                    viewModel.toggleAbout()
                }
            )
        },
        drawerState = drawerState,
        gesturesEnabled = false
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("XposedFakeLocation")},
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
                        }
                    }
                )
            },
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

    // Show Settings Screen
    if (showSettings) {
        SettingsScreen(onClose = { viewModel.toggleSettings() })
    }

    // Show About Screen
    if (showAbout) {
        AboutScreen(onClose = { viewModel.toggleAbout() })
    }
}