package com.example.androidmaiden.screens.settings.appearance

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Res.stringResource
import com.example.androidmaiden.screens.SettingsGroup
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ChatPersonalizationSettingsGroup() {
    var chatBgColor by remember { mutableStateOf(Color.Transparent) }

    SettingsGroup(title = stringResource(id = "settings_chat_personalization_title")) {
        ChatPreview(backgroundColor = if (chatBgColor == Color.Transparent) MaterialTheme.colorScheme.surface else chatBgColor)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        ChatBackgroundSetting(
            selectedColor = chatBgColor,
            onColorSelected = { chatBgColor = it }
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        UserAvatarSetting()
    }
}

@Composable
private fun ChatPreview(backgroundColor: Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(100.dp),
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                shape = CircleShape,
                modifier = Modifier.align(Alignment.End).size(24.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {}
            Surface(
                shape = CircleShape,
                modifier = Modifier.align(Alignment.Start).size(24.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {}
            Text(
                stringResource(id = "settings_chat_personalization_preview"),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    .padding(4.dp).align(Alignment.End)
            )
        }
    }
}


@Composable
private fun ChatBackgroundSetting(selectedColor: Color, onColorSelected: (Color) -> Unit) {
    val colors = listOf(
        Color.Transparent, // System Default
        MaterialTheme.colorScheme.surfaceVariant,
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Wallpaper,
                contentDescription = stringResource(id = "settings_chat_personalization_background"),
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(stringResource(id = "settings_chat_personalization_background"), style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colors.forEach { color ->
                Surface(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onColorSelected(color) },
                    shape = CircleShape,
                    color = if (color == Color.Transparent) MaterialTheme.colorScheme.surface else color,
                    border = if (selectedColor == color) BorderStroke(
                        2.dp,
                        MaterialTheme.colorScheme.primary
                    ) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    if (color == Color.Transparent) {
                        Icon(
                            Icons.Default.BrightnessAuto,
                            contentDescription = stringResource(id = "settings_chat_personalization_system_default"),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = { /* TODO: Handle image picking */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Image,
                contentDescription = stringResource(id = "settings_chat_personalization_choose_image"),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(id = "settings_chat_personalization_choose_from_gallery"))
        }
    }
}

@Composable
private fun UserAvatarSetting() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = stringResource(id = "settings_chat_personalization_your_avatar"),
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(stringResource(id = "settings_chat_personalization_your_avatar"), style = MaterialTheme.typography.bodyLarge)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(id = "user_avatar_name"),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { /* TODO: Handle avatar upload */ }) {
                    Text(stringResource(id = "settings_chat_personalization_upload_avatar_image"))
                }
            }
        }
    }
}
