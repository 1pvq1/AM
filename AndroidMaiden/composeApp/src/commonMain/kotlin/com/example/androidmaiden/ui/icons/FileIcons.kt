package com.example.androidmaiden.ui.icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androidmaiden.screenPages.FileType

@Composable
expect fun fileTypeIcon(type: FileType): ImageVector?
