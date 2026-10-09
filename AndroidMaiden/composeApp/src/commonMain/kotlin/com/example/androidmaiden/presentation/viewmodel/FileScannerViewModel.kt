package com.example.androidmaiden.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.core.experimental.time.TimeProvider
import com.example.androidmaiden.data.repository.FileRepository
import com.example.androidmaiden.data.repository.SettingsRepository
import com.example.androidmaiden.domain.model.*
import com.example.androidmaiden.domain.service.AndroidFolderExplainer
import com.example.androidmaiden.core.util.FileTypeUtils
import com.example.androidmaiden.presentation.ui.features.eg.simFileNode
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * Data class representing statistics for a folder's content.
 *
 * @param typeDistribution Map of file extensions/types to total bytes.
 * @param totalSize Total byte size of all files in the current folder.
 * @param fileCount Total number of files in the current folder.
 * @param folderCount Total number of sub-folders in the current folder.
 */
data class FolderAnalysisStats(
    val typeDistribution: Map<String, Long> = emptyMap(),
    val totalSize: Long = 0L,
    val fileCount: Int = 0,
    val folderCount: Int = 0
)

/**
 * Filter extension function to filter [FileSysNode] lists based on [HiddenFilterMode].
 */
fun List<FileSysNode>.filterByHiddenMode(mode: HiddenFilterMode): List<FileSysNode> {
    return when (mode) {
        HiddenFilterMode.SHOW_ALL -> this
        HiddenFilterMode.EXCLUDE_HIDDEN -> filter { !it.name.startsWith(".") }
        HiddenFilterMode.ONLY_HIDDEN -> filter { it.name.startsWith(".") }
    }
}

/**
 * ViewModel for the File System Analysis screen.
 * Handles navigation, file operations, simulated and real data mapping, content analysis, and hidden file filtering.
 */
class FileScannerViewModel(
    val repository: FileRepository,
    private val timeProvider: TimeProvider,
    private val settingsRepository: SettingsRepository? = null
) : BaseViewModel() {

    private val defaultRoot: String get() = repository.getScannedPath()
    private val mockRootPath = "/"

    /**
     * The current directory node being displayed.
     */
    var currentDirectory by mutableStateOf<FileSysNode?>(null)
        private set

    /**
     * Current system hidden files filter mode (Show All, Exclude Hidden, or Hidden Only).
     */
    var hiddenFilterMode by mutableStateOf(HiddenFilterMode.SHOW_ALL)
        private set

    /**
     * Statistics for the current directory's content.
     */
    var folderStats by mutableStateOf(FolderAnalysisStats())
        private set

    /**
     * Whether to use mock data for demonstration purposes.
     */
    var useMock by mutableStateOf(true)
        private set

    /**
     * The navigation stack of paths to support "back" functionality and breadcrumbs.
     */
    private val _pathStack = mutableStateListOf<String>()
    val pathStack: List<String> get() = _pathStack

    /**
     * Observes real data from the repository for the current path.
     */
    private var realDataJob: Job? = null

    init {
        _isLoading.value = true
        loadDirectory(if (useMock) mockRootPath else defaultRoot)

        settingsRepository?.let { settings ->
            viewModelScope.launch {
                settings.controlAppearanceConfig.collect { config ->
                    updateHiddenFilterMode(config.hiddenFilterMode)
                }
            }
        }
    }

    /**
     * Updates the hidden files filter mode and recalculates stats.
     */
    fun updateHiddenFilterMode(mode: HiddenFilterMode) {
        hiddenFilterMode = mode
        currentDirectory?.let { dir ->
            val filteredChildren = dir.children.filterByHiddenMode(mode)
            calculateStats(filteredChildren)
        }
    }

    /**
     * Triggers a manual sync of the real file system.
     */
    fun startSync() {
        if (!useMock) {
            repository.startIncrementalSync()
        }
    }

    /**
     * Toggles between mock data and real device data from the Room database.
     */
    fun toggleSource() {
        useMock = !useMock
        _pathStack.clear()
        loadDirectory(if (useMock) mockRootPath else defaultRoot)
    }

    /**
     * Navigates into a sub-folder.
     * Prevents duplicate path stacking and ensures navigation only happens for folders.
     *
     * @param node The folder node to enter.
     */
    fun navigateTo(node: FileSysNode) {
        if (node.isFolder) {
            val targetPath = node.path ?: node.name
            if (_pathStack.lastOrNull() != targetPath) {
                _pathStack.add(targetPath)
                loadDirectory(targetPath)
            }
        }
    }

    /**
     * Navigates to a specific folder in the path stack (Breadcrumb segment click).
     *
     * @param index The index in the path stack to jump to, or -1 for root.
     */
    fun navigateToStackIndex(index: Int) {
        if (index >= 0 && index < _pathStack.size) {
            val targetPath = _pathStack[index]
            while (_pathStack.size > index + 1) {
                _pathStack.removeAt(_pathStack.size - 1)
            }
            loadDirectory(targetPath)
        } else if (index == -1) {
            _pathStack.clear()
            loadDirectory(if (useMock) mockRootPath else defaultRoot)
        }
    }

    /**
     * Navigates back to the parent directory.
     *
     * @return True if back navigation was handled within the stack, false if at root.
     */
    fun navigateBack(): Boolean {
        if (_pathStack.isNotEmpty()) {
            _pathStack.removeAt(_pathStack.size - 1)
            val parentPath = if (_pathStack.isEmpty()) {
                if (useMock) mockRootPath else defaultRoot
            } else {
                _pathStack.last()
            }
            loadDirectory(parentPath)
            return true
        }
        return false
    }

    /**
     * Loads a directory by path and triggers analysis.
     *
     * @param path The absolute path or simulated path to load.
     */
    fun loadDirectory(path: String = defaultRoot) {
        _isLoading.value = true
        _error.value = null
        realDataJob?.cancel()

        if (useMock) {
            loadMockDirectory(path)
        } else {
            loadRealDirectory(path)
        }
    }

    /**
     * Loads mock directory data by searching for the target path in the simulated tree hierarchy.
     *
     * @param path The path to resolve in the mock file system.
     */
    private fun loadMockDirectory(path: String) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val fullTree = simFileNode()
                val targetNode = if (path == mockRootPath || path.isBlank()) {
                    fullTree
                } else {
                    findMockNode(fullTree, path) ?: fullTree
                }

                withContext(Dispatchers.Main) {
                    currentDirectory = targetNode
                    calculateStats(targetNode.children.filterByHiddenMode(hiddenFilterMode))
                }
            } catch (e: Exception) {
                _error.value = "Mock data error: ${e.message}"
            } finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

    /**
     * Recursively searches for a node matching the target path in a simulated tree hierarchy.
     *
     * @param root The current root node to search within.
     * @param targetPath The target path string to find.
     * @return The matching [FileSysNode], or null if not found.
     */
    private fun findMockNode(root: FileSysNode, targetPath: String): FileSysNode? {
        if (root.path == targetPath || root.name == targetPath) {
            return root
        }
        for (child in root.children) {
            val found = findMockNode(child, targetPath)
            if (found != null) {
                return found
            }
        }
        return null
    }

    /**
     * Loads real directory data by querying real-time OS file listing and observing database flow.
     *
     * @param path The physical directory path on the device.
     */
    private fun loadRealDirectory(path: String) {
        viewModelScope.launch(Dispatchers.Default) {
            // First: Immediately load real-time directory listing from OS for instant feedback
            try {
                val realTimeItems = repository.getRealTimeFiles(path)
                if (realTimeItems.isNotEmpty()) {
                    val realTimeNode = mapMetadataToNode(path, realTimeItems)
                    withContext(Dispatchers.Main) {
                        currentDirectory = realTimeNode
                        calculateStats(realTimeNode.children.filterByHiddenMode(hiddenFilterMode))
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                // Fallback gracefully to database flow if direct OS listing fails
            }

            // Second: Observe Room database flow for background updates
            realDataJob = repository.getFilesByParent(path)
                .onEach { metadataList ->
                    val sourceItems = if (metadataList.isNotEmpty()) {
                        metadataList
                    } else {
                        repository.getRealTimeFiles(path)
                    }
                    val node = mapMetadataToNode(path, sourceItems)
                    withContext(Dispatchers.Main) {
                        currentDirectory = node
                        calculateStats(node.children.filterByHiddenMode(hiddenFilterMode))
                        _isLoading.value = false
                    }
                }
                .catch { e ->
                    withContext(Dispatchers.Main) {
                        if (currentDirectory == null) {
                            _error.value = "Database error: ${e.message}"
                        }
                        _isLoading.value = false
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    /**
     * Calculates size distribution and file/folder counts for the given nodes.
     *
     * @param nodes The child nodes to aggregate stats for.
     */
    private fun calculateStats(nodes: List<FileSysNode>) {
        val distribution = mutableMapOf<String, Long>()
        var totalSize = 0L
        var fileCount = 0
        var folderCount = 0

        nodes.forEach { node ->
            if (node.isFolder) {
                folderCount++
            } else {
                fileCount++
                val size = node.size ?: 0L
                totalSize += size
                val type = FileTypeUtils.getExtensionType(node.name)
                distribution[type] = distribution.getOrPut(type) { 0L } + size
            }
        }
        folderStats = FolderAnalysisStats(distribution, totalSize, fileCount, folderCount)
    }

    /**
     * Deletes a file or folder in real data mode.
     *
     * @param node The node to delete.
     */
    fun deleteNode(node: FileSysNode) {
        if (useMock) return
        viewModelScope.launch {
            node.path?.let { path ->
                repository.deleteFile(path)
            }
        }
    }

    /**
     * Renames a file or folder in real data mode.
     *
     * @param node The node to rename.
     * @param newName The new name for the file or folder.
     */
    fun renameNode(node: FileSysNode, newName: String) {
        if (useMock) return
        viewModelScope.launch {
            node.path?.let { path ->
                repository.renameFile(path, newName)
            }
        }
    }

    /**
     * Maps database metadata or real-time items to [FileSysNode] instances enriched with Android architectural explanations
     * and sub-folder child nodes for accurate counting.
     *
     * @param path The parent directory path.
     * @param metadataList The list of child metadata or real-time items.
     * @return The parent [FileSysNode] containing child nodes with populated sub-children.
     */
    private fun mapMetadataToNode(path: String, metadataList: List<FileItem>): FileSysNode {
        val children = metadataList.map { metadata ->
            val descriptionText = if (metadata.isDirectory) {
                AndroidFolderExplainer.explainFolder(metadata.path, metadata.name)
            } else {
                "${metadata.size / 1024} KB"
            }

            // Populate immediate sub-folder child nodes so FileItem accurately computes folder & file counts
            val subFolderChildren = if (metadata.isDirectory && !metadata.path.isNullOrBlank()) {
                try {
                    val subItems = repository.getRealTimeFiles(metadata.path)
                    subItems.map { subItem ->
                        FileSysNode(
                            name = subItem.name,
                            size = if (subItem.isDirectory) null else subItem.size,
                            lastModified = subItem.lastModified,
                            nodeType = if (subItem.isDirectory) NodeType.FOLDER else NodeType.FILE,
                            folderType = if (subItem.isDirectory) FolderType.FOLDER else FolderType.OTHER,
                            dataSource = DataSource.REAL,
                            path = subItem.path
                        )
                    }
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }

            FileSysNode(
                name = metadata.name,
                size = if (metadata.isDirectory) null else metadata.size,
                lastModified = metadata.lastModified,
                nodeType = if (metadata.isDirectory) NodeType.FOLDER else NodeType.FILE,
                folderType = if (metadata.isDirectory) FolderType.FOLDER else FolderType.OTHER,
                dataSource = DataSource.REAL,
                path = metadata.path,
                description = descriptionText,
                children = subFolderChildren
            )
        }

        val folderName = if (path == defaultRoot || path == "/" || path == mockRootPath) {
            "Internal Storage"
        } else {
            path.substringAfterLast("/")
        }
        val parentDescription = AndroidFolderExplainer.explainFolder(path, folderName)

        return FileSysNode(
            name = folderName,
            nodeType = NodeType.FOLDER,
            folderType = FolderType.FOLDER,
            dataSource = DataSource.REAL,
            children = children,
            lastModified = timeProvider.nowMillis(),
            description = parentDescription,
            path = path
        )
    }
}
