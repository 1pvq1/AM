package com.example.androidmaiden.Res

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun stringResource(id: String): String {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(id, "string", context.packageName)
    return if (resourceId != 0) {
        context.getString(resourceId)
    } else {
        ""
    }
}

@Composable
actual fun stringResource(id: String, vararg formatArgs: Any): String {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(id, "string", context.packageName)
    return if (resourceId != 0) {
        context.getString(resourceId, *formatArgs)
    } else {
        ""
    }
}
