package com.example.androidmaiden.mods

import android.util.Log
import com.example.androidmaiden.screenPages.FileNode
import java.io.File

actual fun listFiles(path: String): List<FileNode> {
    val dir = File(path)
    if (!dir.exists() || !dir.isDirectory) return emptyList()
    return dir.listFiles()?.map {
        FileNode(
            name = it.name,
            isDirectory = it.isDirectory,
//            children = emptyList(), // 先不递归，避免卡顿

            // ⚠️ 这里要填充一层子项，否则 children 永远是空
            children = if (it.isDirectory) {
                it.listFiles()?.map { child ->
                    FileNode(
                        name = child.name,
                        isDirectory = child.isDirectory,
                        children = emptyList(), // 只加载一层
                        isMock = false
                    )
                } ?: emptyList()
            } else emptyList(),
            description = if (it.isDirectory) "文件夹" else "文件",
            isMock = false
        )
    } ?: emptyList()

//    Log.d("FileDebug", "path=$path, files=${dir.listFiles()?.size}")

}
