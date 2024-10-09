// DrawerContent.kt
package com.noobexon.xposedfakelocation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    onSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Column {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSettingsClick()
                        scope.launch { drawerState.close() }
                    }
                    .padding(16.dp)
            )
            Divider()
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAboutClick()
                        scope.launch { drawerState.close() }
                    }
                    .padding(16.dp)
            )
        }
    }
}
