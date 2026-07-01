package com.example.androidmaiden.presentation.ui.screens.themeMatching.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.themematching.model.CharacterAction
import com.example.androidmaiden.domain.themematching.model.CharacterStats
import com.example.androidmaiden.domain.themematching.model.AnimationConfig

/**
 * Grid line color used for spreadsheet-style tables.
 */
private val gridLineColor: Color
    @Composable
    get() = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

/**
 * Style 1: Modern clean table with zebra striping and a prominent header.
 *
 * @param activeAction The currently selected CharacterAction to highlight.
 * @param configs The list of animation configurations retrieved from domain.
 */
@Composable
fun AnimationConfigTable(
    activeAction: CharacterAction,
    configs: List<AnimationConfig>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
    ) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(12.dp)
        ) {
            TableHeaderCell("Action", Modifier.weight(1f))
            TableHeaderCell("Speed (ms)", Modifier.weight(1f))
            TableHeaderCell("Loop", Modifier.weight(0.7f))
            TableHeaderCell("Easing Style", Modifier.weight(1.3f))
        }

        // Table Rows
        configs.forEachIndexed { index, config ->
            val isCurrent = config.action == activeAction
            val backgroundColor = when {
                isCurrent -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                index % 2 == 0 -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isCurrent) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Active",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                    Text(
                        text = config.action.label,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }

                TableCell(config.speedMs, Modifier.weight(1f))
                TableCell(if (config.isLoop) "Yes" else "No", Modifier.weight(0.7f))
                TableCell(config.easingStyle, Modifier.weight(1.3f))
            }
        }
    }
}

/**
 * Style 2: Tech-Grid layout table with visible grid borders, resembling spreadsheet tables.
 *
 * @param stats The list of stats attributes retrieved from domain.
 */
@Composable
fun CharacterStatsTable(stats: List<CharacterStats>) {
    val currentGridLineColor = gridLineColor

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
    ) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(12.dp)
        ) {
            TableHeaderCell("Attribute", Modifier.weight(1f), color = MaterialTheme.colorScheme.onSecondaryContainer)
            TableHeaderCell("Value", Modifier.weight(1f), color = MaterialTheme.colorScheme.onSecondaryContainer)
            TableHeaderCell("Modifier", Modifier.weight(1.5f), color = MaterialTheme.colorScheme.onSecondaryContainer)
        }

        // List of stats
        stats.forEachIndexed { index, stat ->
            val rowBg = if (index % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(rowBg)
                    .drawBehind {
                        // Bottom cell border
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = currentGridLineColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = strokeWidth
                        )
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stat.attribute, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(stat.value, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                
                // Modifier chip layout inside table
                Box(
                    modifier = Modifier
                        .weight(1.5f)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stat.modifier,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Style 3: Palette table style displaying primary system colors matching themes.
 */
@Composable
fun ColorPaletteTable() {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
    ) {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(12.dp)
        ) {
            TableHeaderCell("Color Role", Modifier.weight(1.0f), color = MaterialTheme.colorScheme.onTertiaryContainer)
            TableHeaderCell("Preview", Modifier.weight(0.8f), color = MaterialTheme.colorScheme.onTertiaryContainer)
            TableHeaderCell("Hex Code (Tap to Copy)", Modifier.weight(1.5f), color = MaterialTheme.colorScheme.onTertiaryContainer)
            TableHeaderCell("Usage Context", Modifier.weight(1.5f), color = MaterialTheme.colorScheme.onTertiaryContainer)
        }

        val primaryColor = MaterialTheme.colorScheme.primary
        val secondaryColor = MaterialTheme.colorScheme.secondary
        val tertiaryColor = MaterialTheme.colorScheme.tertiary
        val errorColor = MaterialTheme.colorScheme.error

        val palette = listOf(
            Triple("Primary", primaryColor, "Brand base & major UI actions"),
            Triple("Secondary", secondaryColor, "Subtle components & navigation"),
            Triple("Tertiary", tertiaryColor, "Secondary callouts & badge details"),
            Triple("Error Color", errorColor, "Warnings & critical status elements")
        )

        palette.forEachIndexed { index, (role, color, usage) ->
            val rowBg = if (index % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
            val hexString = formatColorToHex(color)
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(rowBg)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(hexString))
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(role, Modifier.weight(1.0f), bold = true)

                // Color Box Preview
                Box(
                    modifier = Modifier
                        .weight(0.8f)
                        .height(18.dp)
                        .padding(end = 12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color)
                        .border(1.dp, Color.Black.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                )

                TableCell(hexString, Modifier.weight(1.5f))
                TableCell(usage, Modifier.weight(1.5f))
            }
        }
    }
}

/**
 * Standard table header cell rendering bold text.
 *
 * @param text The string text to display inside the header cell.
 * @param modifier Layout modifiers for the cell container.
 * @param color The text color to apply.
 */
@Composable
fun TableHeaderCell(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier
    )
}

/**
 * Standard table body cell rendering regular text.
 *
 * @param text The string text to display inside the table cell.
 * @param modifier Layout modifiers for the cell container.
 * @param bold Whether to render the cell text in bold style.
 */
@Composable
fun TableCell(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = false
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

/**
 * Utility helper to convert a Jetpack Compose Color object to its string hexadecimal code format.
 *
 * @param color The Color instance to format.
 * @return A string hex formatted color representation (e.g. #FF00FF).
 */
fun formatColorToHex(color: Color): String {
    val r = (color.red * 255f).toInt().coerceIn(0, 255)
    val g = (color.green * 255f).toInt().coerceIn(0, 255)
    val b = (color.blue * 255f).toInt().coerceIn(0, 255)
    val hexChars = "0123456789ABCDEF"
    fun toHexByte(v: Int) = "${hexChars[(v shr 4) and 0x0F]}${hexChars[v and 0x0F]}"
    return "#${toHexByte(r)}${toHexByte(g)}${toHexByte(b)}"
}
