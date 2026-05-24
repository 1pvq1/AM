package com.example.androidmaiden.platform

import androidx.compose.runtime.Composable

@Composable
expect fun stringResource(id: String): String

@Composable
expect fun stringResource(id: String, vararg formatArgs: Any): String
