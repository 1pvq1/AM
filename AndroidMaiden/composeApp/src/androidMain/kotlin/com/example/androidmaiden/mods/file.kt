package com.example.androidmaiden.mods

import android.util.Log
import com.example.androidmaiden.views.fileSys.*
import java.io.File
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
actual fun listFiles(path: String): List<FileNode> {
    val dir = File(path)
    if (!dir.exists() || !dir.isDirectory) return emptyList()

    return dir.listFiles()?.map { file ->
        FileNode(
            name = file.name,
            size = if (file.isFile) file.length() else null,
            lastModified = file.lastModified().takeIf { it > 0 } ?: Clock.System.now().toEpochMilliseconds(),
            nodeType = if (file.isDirectory) NodeType.FOLDER else NodeType.FILE,
            folderType = if (file.isDirectory) FolderType.FOLDER else FolderType.OTHER,
            description = if (file.isDirectory) "文件夹" else "文件",
            dataSource = DataSource.REAL,
            // 只加载一层子项，避免递归卡顿
            children = if (file.isDirectory) {
                file.listFiles()?.map { child ->
                    FileNode(
                        name = child.name,
                        size = if (child.isFile) child.length() else null,
                        lastModified = child.lastModified().takeIf { it > 0 } ?: Clock.System.now().toEpochMilliseconds(),
                        nodeType = if (child.isDirectory) NodeType.FOLDER else NodeType.FILE,
                        folderType = if (child.isDirectory) FolderType.FOLDER else FolderType.OTHER,
                        description = if (child.isDirectory) "文件夹" else "文件",
                        dataSource = DataSource.REAL,
                        children = emptyList(),
                        path = child.absolutePath
                    )
                } ?: emptyList()
            } else emptyList(),
            path = file.absolutePath
        )
    } ?: emptyList()
}
