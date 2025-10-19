package com.example.androidmaiden.mods

import com.example.androidmaiden.screenPages.FileNode

expect fun listFiles(path: String): List<FileNode>
