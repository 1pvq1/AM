package com.example.androidmaiden.platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Platform-specific provider for DataStore instance.
 */
expect fun createDataStore(context: Any? = null): DataStore<Preferences>

internal const val DATASTORE_FILE_NAME = "settings.preferences_pb"
