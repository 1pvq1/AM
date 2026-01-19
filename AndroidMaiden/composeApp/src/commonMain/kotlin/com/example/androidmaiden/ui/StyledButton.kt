package com.example.androidmaiden.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.ButtonDisplayStyle
import com.example.androidmaiden.LocalButtonDisplayStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector,
    text: String,
    tooltip: String
) {
    val style = LocalButtonDisplayStyle.current

    when (style) {
        ButtonDisplayStyle.ICON_AND_TEXT -> {
            FilledTonalButton(onClick = onClick, modifier = modifier, enabled = enabled) {
                Icon(icon, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text(text)
            }
        }
        ButtonDisplayStyle.ICON_ONLY -> {
            TooltipBox(
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = { PlainTooltip { Text(tooltip) } },
                state = rememberTooltipState(),
            ) {
                IconButton(onClick = onClick, modifier = modifier, enabled = enabled) {
                    Icon(icon, contentDescription = text)
                }
            }
        }
        ButtonDisplayStyle.TEXT_ONLY -> {
            Button(onClick = onClick, modifier = modifier, enabled = enabled) {
                Text(text)
            }
        }
    }
}
