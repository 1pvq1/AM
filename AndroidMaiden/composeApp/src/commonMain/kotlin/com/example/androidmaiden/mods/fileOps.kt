package com.example.androidmaiden.mods

import com.example.androidmaiden.model.FileSysNode

expect fun listFiles(path: String): List<FileSysNode>
