package com.example.androidmaiden.utils

import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.model.FileCategory
import com.example.androidmaiden.utils.FileTypeUtils.getExtensionType

class deprecated {


    /**
     * Logic to group raw Database Metadata into UI-friendly Categories.
     */
    private fun classifyMetadata(list: List<FileMetadata>): List<FileCategory> {
        val groups = list.filter { !it.isDirectory }.groupBy { getExtensionType(it.name) }

        /*        return listOf(
                    mapToCategory("Images", Icons.Default.Image, "Images", groups["Images"]),
                    mapToCategory("Videos", Icons.Default.Videocam, "Videos", groups["Videos"]),
                    mapToCategory("Audio", Icons.Default.MusicNote, "Audio", groups["Audio"]),
                    mapToCategory("Documents", Icons.Default.Description, "Documents", groups["Documents"]),
                    mapToCategory("APKs", Icons.Default.Android, "APKs", groups["APKs"]),
                    mapToCategory("Archives", Icons.Default.Archive, "Archives", groups["Archives"]),
                    mapToCategory("Other", Icons.AutoMirrored.Filled.InsertDriveFile, "Other", groups["Other"])
                )*/

        // Map through the definitions to build the categories
        return FileTypeUtils.categoryDefinitions.map { def ->
            val items = groups[def.type] ?: emptyList()
            FileCategory(
                name = def.name,
                icon = def.icon,
                type = def.type,
                count = items.size,
                totalSizeMb = items.sumOf { it.size } / (1024 * 1024),
                files = items
            )
        }
    }
}