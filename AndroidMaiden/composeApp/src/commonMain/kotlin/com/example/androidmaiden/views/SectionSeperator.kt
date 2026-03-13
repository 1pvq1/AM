package com.example.androidmaiden.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Enhanced section header with a title and a subtle divider.
 */
@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
        )
    }
}


/**
 * A generic sticky header that adapts its background to the surface
 * to ensure files don't bleed through while scrolling.
 * Updated to support an optional count display.
 */
@Composable
fun StickySectionHeader(
    title: String,
    count: Int? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        // Use a solid color (no alpha) to prevent ghosting of items behind the header
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                if (count != null) {
                    Text(
                        text = "$count",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            // A thin divider helps distinguish the section visually
            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * A sticky header design suitable for timelines.
 */
@Composable
fun StickyDateHeader(dateText: String, count: Int? = null, modifier: Modifier = Modifier) {
    StickySectionHeader(title = dateText, count = count, modifier = modifier)
}

/**
 * A refined divider for list items.
 */
@Composable
fun ItemSeparator(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = 16.dp),
        thickness = 0.5.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    )
}

@Preview(showBackground = true)
@Composable
fun SectionSeperatorPreview() {
    Column {
        SectionHeader("Images")
        StickyDateHeader("OCTOBER 2023", count = 125)
        ItemSeparator()
    }
}
