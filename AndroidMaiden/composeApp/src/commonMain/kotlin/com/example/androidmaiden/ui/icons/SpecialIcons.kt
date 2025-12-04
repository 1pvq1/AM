package com.example.androidmaiden.ui.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

//@Composable
//expect fun folderTypeIcon(type: FolderType): ImageVector?

enum class ToolbarIcon { LIST, GRID, TREE, SORT }

@Composable
expect fun toolbarIcon(icon: ToolbarIcon): ImageVector?

//@Composable
//expect fun m3Icons(icon: m3): ImageVector?