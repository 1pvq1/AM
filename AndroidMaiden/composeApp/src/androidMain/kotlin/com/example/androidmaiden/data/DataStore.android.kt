package com.example.androidmaiden.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

/**
 * Android implementation for creating DataStore.
 */
actual fun createDataStore(context: Any?): DataStore<Preferences> {
    require(context is Context) { "Android DataStore requires a Context" }
    return androidx.datastore.preferences.core.PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile(DATASTORE_FILE_NAME) }
    )
}
