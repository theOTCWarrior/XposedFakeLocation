package com.noobexon.xposedfakelocation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Discord
import compose.icons.lineawesomeicons.Github
import compose.icons.lineawesomeicons.InfoCircleSolid
import compose.icons.lineawesomeicons.QuestionCircle
import compose.icons.lineawesomeicons.Telegram

@Composable
fun DrawerContent(
    onSettingsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    ModalDrawerSheet {
        Column (
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Text(
                text = "XposedFakeLocation",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(12.dp)
            )
            DrawerItem(
                icon = LineAwesomeIcons.InfoCircleSolid,
                label = "About",
                onClick = { onAboutClick() }
            )
            DrawerItem(
                icon =  LineAwesomeIcons.QuestionCircle,
                label = "How-to-Use",
                onClick = { }
            )
            DrawerItem(
                icon =  LineAwesomeIcons.Telegram,
                label = "Telegeram",
                onClick = { }
            )
            DrawerItem(
                icon = LineAwesomeIcons.Discord,
                label = "Discord",
                onClick = { }
            )
            DrawerItem(
                icon = LineAwesomeIcons.Github,
                label = "Github",
                onClick = { }
            )
            DrawerItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = { onSettingsClick() }
            )
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}