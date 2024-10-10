package com.noobexon.xposedfakelocation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    // BackHandler to close the drawer when open
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    // Scaffold with drawer
    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        },
        scrimColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.32f), // Custom scrim color
        drawerState = drawerState,
        gesturesEnabled = false,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("XposedFakeLocation") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.triggerCenterMapEvent()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Center")
                        }
                        IconButton(
                            onClick = { /* Implement options menu */ }
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
}
