package com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidmaiden.domain.model.SubcategoryInfo

/**
 * Horizontal scrollable filter chips displaying subcategories with file counts.
 *
 * @param subcategories List of subcategory models with stats.
 * @param selectedSubcategoryId ID of the currently selected subcategory, or null/empty for All.
 * @param onSelectSubcategory Callback when a subcategory chip is clicked.
 * @param modifier Modifier to be applied to the container.
 */
@Composable
fun CategorySubcategoryTabs(
    subcategories: List<SubcategoryInfo>,
    selectedSubcategoryId: String?,
    onSelectSubcategory: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (subcategories.isEmpty()) return

    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        subcategories.forEach { sub ->
            val isSelected = if (selectedSubcategoryId.isNullOrBlank()) {
                sub.id.endsWith("_all")
            } else {
                selectedSubcategoryId == sub.id
            }

            FilterChip(
                selected = isSelected,
                onClick = {
                    val nextId = if (sub.id.endsWith("_all")) null else sub.id
                    onSelectSubcategory(nextId)
                },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = sub.displayName,
                            style = MaterialTheme.typography.labelMedium
                        )
                        if (sub.count > 0) {
                            Text(
                                text = "(${sub.count})",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                },
                leadingIcon = sub.icon?.let { icon ->
                    {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            )
        }
    }
}
