package com.example.androidmaiden.mods

import com.example.androidmaiden.model.FileNode
import com.example.androidmaiden.model.NodeType
import java.io.File

actual fun listFiles(path: String): List<FileNode> {
    val directory = File(path)
    if (!directory.exists() || !directory.isDirectory) {
        return emptyList()
    }
    return directory.listFiles()?.map { file ->
        FileNode(
            name = file.name,
            path = file.absolutePath,
            nodeType = if (file.isDirectory) NodeType.FOLDER else NodeType.FILE,
            size = if (file.isFile) file.length() else null,
            lastModified = file.lastModified()
        )
    } ?: emptyList()
}
