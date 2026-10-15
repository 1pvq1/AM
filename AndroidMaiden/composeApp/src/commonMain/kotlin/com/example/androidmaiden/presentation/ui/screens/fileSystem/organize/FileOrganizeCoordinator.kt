package com.example.androidmaiden.presentation.ui.screens.fileSystem.organize

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeClass
import com.example.androidmaiden.presentation.ui.adaptive.WindowSizeCategory
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.ui.theme.core.AppThemeType
import com.example.androidmaiden.presentation.ui.theme.core.ButtonDisplayStyle
import com.example.androidmaiden.presentation.ui.theme.core.ThemeMode
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

/**
 * Adaptive Coordinator for the File Organize feature.
 *
 * @param windowSizeClass The current window size class of the device.
 * @param allTags List of all available tags.
 * @param selectedTag The currently selected tag for filtering.
 * @param taggedFiles List of files associated with the selected tag.
 * @param searchQuery The current search query string.
 * @param searchResults List of files matching the search query.
 * @param pathStack The current navigation path stack.
 * @param currentDirectoryFiles List of files in the current directory with their tags.
 * @param onBack Callback for navigating back.
 * @param onSelectTag Callback when a tag is selected or deselected.
 * @param onDeleteTag Callback to delete a tag.
 * @param onUpdateTag Callback to update a tag's properties.
 * @param onUpdateSearchQuery Callback when the search query is updated.
 * @param onAddTagToFile Callback to add a tag to a file.
 * @param onRemoveTagFromFile Callback to remove a tag from a file.
 * @param onCreateTag Callback to create a new tag.
 * @param onIndexClick Callback to navigate to a specific index in the path stack.
 * @param onRootClick Callback to navigate to the root directory.
 * @param onFileClick Callback when a file or directory is clicked.
 */
@ExperimentalTime
@Composable
fun FileOrganizeCoordinator(
    windowSizeClass: WindowSizeClass,
    allTags: List<Tag>,
    selectedTag: Tag?,
    taggedFiles: List<FileItem>,
    searchQuery: String,
    searchResults: List<FileItem>,
    pathStack: List<String>,
    currentDirectoryFiles: List<FileWithTags>,
    onBack: () -> Unit,
    onSelectTag: (Tag?) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    onUpdateTag: (Tag) -> Unit,
    onUpdateSearchQuery: (String) -> Unit,
    onAddTagToFile: (FileItem, Tag) -> Unit,
    onRemoveTagFromFile: (FileItem, Tag) -> Unit,
    onCreateTag: (String, String) -> Unit,
    onIndexClick: (Int) -> Unit,
    onRootClick: () -> Unit,
    onFileClick: (FileWithTags) -> Unit
) {
    // Organize UI currently uses a standard layout.
    // Future adaptive layouts for Expanded screens could include a persistent side panel for tags.
    FileOrganizeContent(
        allTags = allTags,
        selectedTag = selectedTag,
        taggedFiles = taggedFiles,
        searchQuery = searchQuery,
        searchResults = searchResults,
        pathStack = pathStack,
        currentDirectoryFiles = currentDirectoryFiles,
        onBack = onBack,
        onSelectTag = onSelectTag,
        onDeleteTag = onDeleteTag,
        onUpdateTag = onUpdateTag,
        onUpdateSearchQuery = onUpdateSearchQuery,
        onAddTagToFile = onAddTagToFile,
        onRemoveTagFromFile = onRemoveTagFromFile,
        onCreateTag = onCreateTag,
        onIndexClick = onIndexClick,
        onRootClick = onRootClick,
        onFileClick = onFileClick
    )
}

// --- Visual Comparison Previews Across 3 Size Classes ---

private val sampleTags = listOf(
    Tag(id = 1L, name = "Important", colorHex = "#FF5722"),
    Tag(id = 2L, name = "Work", colorHex = "#2196F3")
)

@OptIn(ExperimentalTime::class)
private val sampleFileItem = FileItem(
    path = "/storage/emulated/0/Work/report.pdf",
    name = "report.pdf",
    isDirectory = false,
    lastModified = 0L,
    size = 1_200_000L,
    parentPath = "/storage/emulated/0/Work",
    mimeType = "application/pdf"
)

private val sampleCurrentDirectoryFiles = listOf(
    FileWithTags(
        file = sampleFileItem,
        tags = sampleTags
    )
)

/**
 * Visual Preview 1: Smartphone / Mobile (Compact Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "1. Smartphone (Compact)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun FileOrganizeCoordinatorCompactPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.LIGHT,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileOrganizeCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Compact,
                    heightCategory = WindowSizeCategory.Medium
                ),
                allTags = sampleTags,
                selectedTag = null,
                taggedFiles = emptyList(),
                searchQuery = "",
                searchResults = emptyList(),
                pathStack = listOf("Storage", "Work"),
                currentDirectoryFiles = sampleCurrentDirectoryFiles,
                onBack = {},
                onSelectTag = {},
                onDeleteTag = {},
                onUpdateTag = {},
                onUpdateSearchQuery = {},
                onAddTagToFile = { _, _ -> },
                onRemoveTagFromFile = { _, _ -> },
                onCreateTag = { _, _ -> },
                onIndexClick = {},
                onRootClick = {},
                onFileClick = {}
            )
        }
    }
}

/**
 * Visual Preview 2: Foldable / Extended (Medium Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "2. Foldable / Extended (Medium)", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun FileOrganizeCoordinatorMediumPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileOrganizeCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Medium,
                    heightCategory = WindowSizeCategory.Medium
                ),
                allTags = sampleTags,
                selectedTag = sampleTags.first(),
                taggedFiles = listOf(sampleFileItem),
                searchQuery = "",
                searchResults = emptyList(),
                pathStack = listOf("Storage", "Work"),
                currentDirectoryFiles = sampleCurrentDirectoryFiles,
                onBack = {},
                onSelectTag = {},
                onDeleteTag = {},
                onUpdateTag = {},
                onUpdateSearchQuery = {},
                onAddTagToFile = { _, _ -> },
                onRemoveTagFromFile = { _, _ -> },
                onCreateTag = { _, _ -> },
                onIndexClick = {},
                onRootClick = {},
                onFileClick = {}
            )
        }
    }
}

/**
 * Visual Preview 3: Tablet / Desktop Wide Screen (Expanded Viewport Size Class).
 */
@ExperimentalTime
@Preview(name = "3. Wide Screen (Expanded)", widthDp = 1200, heightDp = 800, showBackground = true)
@Composable
fun FileOrganizeCoordinatorExpandedPreview() {
    AppTheme(
        themeType = AppThemeType.DEFAULT,
        themeMode = ThemeMode.DARK,
        useDynamicColor = false,
        buttonDisplayStyle = ButtonDisplayStyle.ICON_AND_TEXT
    ) {
        Surface {
            FileOrganizeCoordinator(
                windowSizeClass = WindowSizeClass(
                    widthCategory = WindowSizeCategory.Expanded,
                    heightCategory = WindowSizeCategory.Expanded
                ),
                allTags = sampleTags,
                selectedTag = null,
                taggedFiles = emptyList(),
                searchQuery = "report",
                searchResults = listOf(sampleFileItem),
                pathStack = listOf("Storage", "Work"),
                currentDirectoryFiles = sampleCurrentDirectoryFiles,
                onBack = {},
                onSelectTag = {},
                onDeleteTag = {},
                onUpdateTag = {},
                onUpdateSearchQuery = {},
                onAddTagToFile = { _, _ -> },
                onRemoveTagFromFile = { _, _ -> },
                onCreateTag = { _, _ -> },
                onIndexClick = {},
                onRootClick = {},
                onFileClick = {}
            )
        }
    }
}

