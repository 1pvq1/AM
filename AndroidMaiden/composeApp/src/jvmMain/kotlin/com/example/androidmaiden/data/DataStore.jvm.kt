package com.example.androidmaiden.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import java.io.File

/**
 * JVM (Desktop) implementation for creating DataStore.
 */
actual fun createDataStore(context: Any?): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        produceFile = {
            // Store in the user's home directory under a hidden folder
            val userHome = System.getProperty("user.home")
            val appDir = File(userHome, ".androidmaiden")
            if (!appDir.exists()) {
                appDir.mkdirs()
            }
            File(appDir, DATASTORE_FILE_NAME)
        }
    )
}
