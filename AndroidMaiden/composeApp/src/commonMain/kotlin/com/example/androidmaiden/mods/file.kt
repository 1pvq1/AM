package com.example.androidmaiden.mods

import com.example.androidmaiden.views.fileSys.FileNode

expect fun listFiles(path: String): List<FileNode>
