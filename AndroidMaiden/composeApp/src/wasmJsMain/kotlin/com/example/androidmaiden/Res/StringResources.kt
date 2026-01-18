package com.example.androidmaiden.Res

import androidx.compose.runtime.Composable

@Composable
actual fun stringResource(id: String): String {
    return id
}

@Composable
actual fun stringResource(id: String, vararg formatArgs: Any): String {
    return id
}
