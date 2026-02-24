package com.example.androidmaiden.mods

import com.example.androidmaiden.model.FileNode

expect fun listFiles(path: String): List<FileNode>
