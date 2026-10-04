package com.example.androidmaiden.presentation.ui.screens.settings.appearance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.features.fileSys.FileItem
import com.example.androidmaiden.presentation.ui.features.fileSys.PathBreadcrumbs
import com.example.androidmaiden.presentation.ui.screens.pages.BasePage
import com.example.androidmaiden.presentation.viewmodel.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Customization page for PathBreadcrumbs, FileItem entries, and future UI controls.
 * Features real-time settings options and an interactive live preview.
 *
 * @param onBack Callback for navigating back to Appearance Settings.
 * @param viewModel ViewModel backing DataStore preferences.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlAppearanceSettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val config by viewModel.controlAppearanceConfig.collectAsState()

    ControlAppearanceSettingsContent(
        config = config,
        onBack = onBack,
        onSeparatorChange = viewModel::setBreadcrumbSeparator,
        onMaxSegmentsChange = viewModel::setBreadcrumbMaxSegments,
        onRootLabelChange = viewModel::setBreadcrumbRootLabel,
        onDescriptionModeChange = viewModel::setFileItemDescriptionMode,
        onIconStyleChange = viewModel::setFileItemIconStyle,
        onShowDetailsChange = viewModel::setFileItemShowDetails
    )
}

/**
 * Stateless content composable for the Control Appearance Settings screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlAppearanceSettingsContent(
    config: ControlAppearanceConfig,
    onBack: () -> Unit,
    onSeparatorChange: (BreadcrumbSeparator) -> Unit,
    onMaxSegmentsChange: (BreadcrumbMaxSegments) -> Unit,
    onRootLabelChange: (BreadcrumbRootLabel) -> Unit,
    onDescriptionModeChange: (FileItemDescriptionMode) -> Unit,
    onIconStyleChange: (FileItemIconStyle) -> Unit,
    onShowDetailsChange: (Boolean) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    BasePage(
        title = "Control Appearance",
        navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
        onNavigationIconClick = onBack,
        scrollBehavior = scrollBehavior
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Interactive Preview Section
            LivePreviewCard(config = config)

            // PathBreadcrumbs Customization Section
            PathBreadcrumbsConfigCard(
                config = config,
                onSeparatorChange = onSeparatorChange,
                onMaxSegmentsChange = onMaxSegmentsChange,
                onRootLabelChange = onRootLabelChange
            )

            // FileItem Entry Customization Section
            FileItemConfigCard(
                config = config,
                onDescriptionModeChange = onDescriptionModeChange,
                onIconStyleChange = onIconStyleChange,
                onShowDetailsChange = onShowDetailsChange
            )
        }
    }
}

/**
 * Interactive preview card demonstrating real-time changes to PathBreadcrumbs and FileItem.
 */
@Composable
private fun LivePreviewCard(config: ControlAppearanceConfig) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Live Interactive Preview",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider()

            // Breadcrumbs Live Preview
            Text(
                text = "PathBreadcrumbs Control:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                PathBreadcrumbs(
                    pathStack = listOf("storage", "emulated", "0", "Android", "data"),
                    onIndexClick = {},
                    onRootClick = {},
                    separatorStyle = config.breadcrumbSeparator,
                    maxSegments = config.breadcrumbMaxSegments,
                    rootLabel = config.breadcrumbRootLabel
                )
            }

            // FileItem Live Preview
            Text(
                text = "File Entry Control:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                FileItem(
                    node = FileSysNode(
                        name = "Android",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.OTHER,
                        description = "Private App Data: Private app storage accessible only by respective owner apps under Scoped Storage rules.",
                        children = listOf(
                            FileSysNode(name = "com.example.app", nodeType = NodeType.FOLDER),
                            FileSysNode(name = "cache.db", nodeType = NodeType.FILE, size = 1024L * 256)
                        ),
                        lastModified = 1715856000000L
                    ),
                    descriptionMode = config.fileItemDescriptionMode,
                    iconStyle = config.fileItemIconStyle,
                    showDetails = config.fileItemShowDetails
                )
            }
        }
    }
}

/**
 * Card containing options for customizing [PathBreadcrumbs].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PathBreadcrumbsConfigCard(
    config: ControlAppearanceConfig,
    onSeparatorChange: (BreadcrumbSeparator) -> Unit,
    onMaxSegmentsChange: (BreadcrumbMaxSegments) -> Unit,
    onRootLabelChange: (BreadcrumbRootLabel) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "PathBreadcrumbs Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // 1. Separator Display Option
            Text("Separator Symbol", style = MaterialTheme.typography.bodyMedium)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                BreadcrumbSeparator.entries.forEachIndexed { index, item ->
                    SegmentedButton(
                        selected = config.breadcrumbSeparator == item,
                        onClick = { onSeparatorChange(item) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = BreadcrumbSeparator.entries.size),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(item.symbol, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            HorizontalDivider()

            // 2. Display Path Length Option
            Text("Visible Path Length", style = MaterialTheme.typography.bodyMedium)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                BreadcrumbMaxSegments.entries.forEachIndexed { index, item ->
                    SegmentedButton(
                        selected = config.breadcrumbMaxSegments == item,
                        onClick = { onMaxSegmentsChange(item) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = BreadcrumbMaxSegments.entries.size),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(item.label.substringBefore(" "), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            HorizontalDivider()

            // 3. Root Label Display Option
            Text("Root Segment Label", style = MaterialTheme.typography.bodyMedium)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                BreadcrumbRootLabel.entries.forEachIndexed { index, item ->
                    SegmentedButton(
                        selected = config.breadcrumbRootLabel == item,
                        onClick = { onRootLabelChange(item) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = BreadcrumbRootLabel.entries.size),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(item.text, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

/**
 * Card containing options for customizing file list entry ([FileItem]).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FileItemConfigCard(
    config: ControlAppearanceConfig,
    onDescriptionModeChange: (FileItemDescriptionMode) -> Unit,
    onIconStyleChange: (FileItemIconStyle) -> Unit,
    onShowDetailsChange: (Boolean) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "File Entry Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // 1. Content Description Mode Option
            Text("Description Display Mode", style = MaterialTheme.typography.bodyMedium)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                FileItemDescriptionMode.entries.forEachIndexed { index, item ->
                    SegmentedButton(
                        selected = config.fileItemDescriptionMode == item,
                        onClick = { onDescriptionModeChange(item) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = FileItemDescriptionMode.entries.size),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(item.label.substringBefore(" "), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            HorizontalDivider()

            // 2. Icon Styling Option
            Text("Icon Theme Style", style = MaterialTheme.typography.bodyMedium)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                FileItemIconStyle.entries.forEachIndexed { index, item ->
                    SegmentedButton(
                        selected = config.fileItemIconStyle == item,
                        onClick = { onIconStyleChange(item) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = FileItemIconStyle.entries.size),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(item.label.substringBefore(" "), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            HorizontalDivider()

            // 3. Show Details Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Show Metadata Details", style = MaterialTheme.typography.bodyMedium)
                    Text("Display timestamp and file size/counts", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                }
                Switch(
                    checked = config.fileItemShowDetails,
                    onCheckedChange = onShowDetailsChange
                )
            }
        }
    }
}

/**
 * Preview for the Control Appearance Settings content.
 */
@Preview
@Composable
fun ControlAppearanceSettingsContentPreview() {
    ControlAppearanceSettingsContent(
        config = ControlAppearanceConfig(),
        onBack = {},
        onSeparatorChange = {},
        onMaxSegmentsChange = {},
        onRootLabelChange = {},
        onDescriptionModeChange = {},
        onIconStyleChange = {},
        onShowDetailsChange = {}
    )
}
