package com.example.androidmaiden.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.model.*
import com.example.androidmaiden.mods.listFiles
import com.example.androidmaiden.viewModels.BaseViewModel
import com.example.androidmaiden.views.eg.simFileNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.io.*
import kotlinx.io.files.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class FileScannerViewModel() : BaseViewModel() {

    val rootPath = "/storage/emulated/0" // Public storage root

    var fileTree by mutableStateOf<FileSysNode?>(null)
        private set

    var useMock by mutableStateOf(true)
        private set

    /**
     *  Only for the screen: File Analysis Screen
     * */
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
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val result = if (useMock) simFileNode()
                else {
                    val children = listFiles(rootPath)
                    FileSysNode(
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
//                _error.value = "Permission denied. Cannot access storage."
            } catch (e: FileNotFoundException) {
                _error.value = "Root directory not found."
            } catch (e: IOException) {
                _error.value = "Error accessing file system: ${e.message}"
            } catch (e: Exception) {
                _error.value = "Unexpected error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
