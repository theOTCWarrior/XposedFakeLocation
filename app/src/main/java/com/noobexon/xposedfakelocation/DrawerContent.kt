package com.noobexon.xposedfakelocation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(
    onSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    ModalDrawerSheet {
        Column {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSettingsClick() }
                    .padding(16.dp)
            )
            Divider()
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAboutClick() }
                    .padding(16.dp)
            )
        }
    }
}

