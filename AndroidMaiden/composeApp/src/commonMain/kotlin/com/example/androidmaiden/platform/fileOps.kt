package com.example.androidmaiden.platform

import com.example.androidmaiden.domain.model.FileSysNode

expect fun listFiles(path: String): List<FileSysNode>
