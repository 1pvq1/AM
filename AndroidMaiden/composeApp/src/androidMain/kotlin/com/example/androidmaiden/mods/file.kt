package com.example.androidmaiden.mods

import com.example.androidmaiden.screenPages.FileNode
import java.io.File

actual fun listFiles(path: String): List<FileNode> {
    val dir = File(path)
    if (!dir.exists() || !dir.isDirectory) return emptyList()
    return dir.listFiles()?.map {
        FileNode(
            name = it.name,
            isDirectory = it.isDirectory,
            children = emptyList(), // 先不递归，避免卡顿
            description = if (it.isDirectory) "文件夹" else "文件"
        )
    } ?: emptyList()
}
