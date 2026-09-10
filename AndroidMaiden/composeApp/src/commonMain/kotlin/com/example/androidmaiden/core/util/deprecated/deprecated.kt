package com.example.androidmaiden.core.util.deprecated

import com.example.androidmaiden.data.local.FileMetadata
import com.example.androidmaiden.core.util.FileTypeUtils

@Deprecated("Use FileItem instead")
fun FileMetadata.extensionGroup(): String = FileTypeUtils.getExtensionType(this.name)
