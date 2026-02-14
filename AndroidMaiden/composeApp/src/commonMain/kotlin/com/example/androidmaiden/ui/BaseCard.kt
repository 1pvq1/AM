package com.example.androidmaiden.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isClickable: Boolean = true,
    trailingIcon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    disabledIconTint: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    horizontalArrangement: Dp = 20.dp
) {
    val cardColors = if (isClickable) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    } else {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    }

    Card(
        onClick = { if (isClickable) onClick() },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, hoveredElevation = 4.dp, pressedElevation = 1.dp),
        colors = cardColors
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(horizontalArrangement)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null, // Decorative
                modifier = Modifier.size(40.dp),
                tint = if (isClickable) iconTint else disabledIconTint
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isClickable) 1f else 0.6f)
                )
            }

            // Only those devices with a jump function will display the "Details" button.
            if (isClickable && trailingIcon != null) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = "Details",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
