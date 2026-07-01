package com.example.androidmaiden.presentation.ui.screens.themeMatching.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Collapsible documentation card explaining pixel art animation concepts.
 *
 * @param expanded Whether the guide content is visible.
 * @param onToggle Invoked when the card header is clicked to expand/collapse the content.
 */
@Composable
fun AnimationGuideCard(
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Pixel Art Animation Guide",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Toggle Guide"
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Text(
                        text = "Creating pixel art animations in Jetpack Compose relies on standard coordinate space transforms applied periodically. To maintain the sharp, pixelated feel, observe the following rules:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    GuideBulletPoint(
                        title = "1. Stepped Interpolation (Stepping)",
                        desc = "Avoid fluid, continuous anti-aliased changes. Use stepped intervals or math rounding (e.g. coerce floats to steps of 4px) to ensure pixels stay strictly aligned on the canvas grid."
                    )
                    GuideBulletPoint(
                        title = "2. Squash & Stretch",
                        desc = "Add dynamism by momentarily squishing characters down (scaleY decreases, scaleX increases) right before a jump or upon landing, and stretching them (scaleY increases, scaleX decreases) while mid-air."
                    )
                    GuideBulletPoint(
                        title = "3. Bobbing & Pivots",
                        desc = "Establish organic movement. Bobbing (vertical translations) conveys weight. Rotating around pivots (e.g., foot coordinates instead of center) mimics real joint rotations."
                    )
                    GuideBulletPoint(
                        title = "4. Secondary Elements",
                        desc = "Enhance readability. Supplement sprite translation with secondary particles, like dust clouds during running, Zzz's when sleeping, or sweat drops for heavy actions."
                    )
                }
            }
        }
    }
}

/**
 * Bullet point view for a single guidelines entry.
 *
 * @param title Bold header text for the guidelines entry.
 * @param desc Explanatory text for the guidelines entry.
 */
@Composable
private fun GuideBulletPoint(title: String, desc: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = desc,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
        )
    }
}

@Preview
@Composable
fun AnimationGuideCardPreview() {
    AppTheme {
        AnimationGuideCard(
            expanded = true,
            onToggle = {}
        )
    }
}
