package com.example.androidmaiden.viewmodel

import androidx.compose.runtime.*
import com.example.androidmaiden.model.*
import com.example.androidmaiden.mods.listFiles
import com.example.androidmaiden.views.eg.simFileNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.io.*
import kotlinx.io.files.FileNotFoundException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class FileScannerViewModel {

    var fileTree by mutableStateOf<FileNode?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var loadError by mutableStateOf<String?>(null)
        private set

    var useMock by mutableStateOf(true)
        private set

    fun toggleSource(){
        useMock = !useMock
        loadRoot(useMock)
    }

    /**
     * Loads the root of the file system either using a simulated tree or actual device data.
     * Reports loading progress and errors to the UI.
     */
    @OptIn(ExperimentalTime::class)
    fun loadRoot(useMock: Boolean = this.useMock) {
        isLoading = true
        loadError = null

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val result = if (useMock) simFileNode()
                else {
                    val children = listFiles("/storage/emulated/0") // Public storage root
                    FileNode(
                        name = "/",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.FOLDER,
                        dataSource = DataSource.REAL,
                        children = children,
                        lastModified = Clock.System.now().toEpochMilliseconds(),
                        description = "Real device root",
                        path = "/storage/emulated/0"
                    )
                }
                fileTree = result
//            } catch (e: SecurityException) {
//                loadError = "Permission denied. Cannot access storage."
            } catch (e: FileNotFoundException) {
                loadError = "Root directory not found."
            } catch (e: IOException) {
                loadError = "Error accessing file system: ${'$'}{e.message}"
            } catch (e: Exception) {
                loadError = "Unexpected error: ${'$'}{e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}