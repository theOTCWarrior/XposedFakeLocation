package com.noobexon.xposedfakelocation.ui.map

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.noobexon.xposedfakelocation.ui.common.components.DrawerContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.noobexon.xposedfakelocation.data.model.FavoriteLocation
import com.noobexon.xposedfakelocation.ui.map.components.AddToFavoritesDialog
import com.noobexon.xposedfakelocation.ui.map.components.MapViewContainer
import com.noobexon.xposedfakelocation.ui.map.components.GoToPointDialog
import com.noobexon.xposedfakelocation.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    mapViewModel: MapViewModel = viewModel()
) {
    val isPlaying by mapViewModel.isPlaying
    val isFabClickable by remember { derivedStateOf { mapViewModel.isFabClickable } }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var showOptionsMenu by remember { mutableStateOf(false) }
    var showGoToPointDialog by remember { mutableStateOf(false) }
    var showAddToFavoritesDialog by remember { mutableStateOf(false) } // Add this line

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
                },
                navController = navController
            )
        },
        scrimColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.32f), // Custom scrim color
        drawerState = drawerState,
        gesturesEnabled = false,
        modifier = Modifier.fillMaxSize()
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
                                mapViewModel.triggerCenterMapEvent()
                            }
                        ) {
                            Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Center")
                        }
                        IconButton(
                            onClick = {
                                showOptionsMenu = true
                            }
                        ) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
                        }
                        DropdownMenu(
                            expanded = showOptionsMenu,
                            onDismissRequest = { showOptionsMenu = false }
                        ) {
                            DropdownMenuItem(
                                leadingIcon = { Icon(imageVector = Icons.Default.LocationSearching, contentDescription = "Go to Point") },
                                text = { Text("Go to Point") },
                                onClick = {
                                    showOptionsMenu = false
                                    showGoToPointDialog = true
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Add to Favorites") },
                                text = { Text("Add to Favorites") },
                                onClick = {
                                    showOptionsMenu = false
                                    showAddToFavoritesDialog = true // Show the Add to Favorites dialog
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = { Icon(imageVector = Icons.Default.Star, contentDescription = "Favorites") },
                                text = { Text("Favorites") },
                                onClick = {
                                    showOptionsMenu = false
                                    navController.navigate(Screen.Favorites.route)
                                }
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (isFabClickable) {
                            mapViewModel.togglePlaying()
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
                MapViewContainer(mapViewModel)
            }
        }

        if (showGoToPointDialog) {
            GoToPointDialog(
                onDismissRequest = { showGoToPointDialog = false },
                onGoToPoint = { latitude, longitude ->
                    mapViewModel.goToPoint(latitude, longitude)
                }
            )
        }

        if (showAddToFavoritesDialog) {
            val lastClickedLocation = mapViewModel.lastClickedLocation.value
            AddToFavoritesDialog(
                onDismissRequest = { showAddToFavoritesDialog = false },
                initialLatitude = lastClickedLocation?.latitude?.toString() ?: "",
                initialLongitude = lastClickedLocation?.longitude?.toString() ?: "",
                onAddFavorite = { name, latitude, longitude ->
                    val favorite = FavoriteLocation(name, latitude, longitude)
                    mapViewModel.addFavoriteLocation(favorite)
                    showAddToFavoritesDialog = false
                }
            )
        }
    }
}
