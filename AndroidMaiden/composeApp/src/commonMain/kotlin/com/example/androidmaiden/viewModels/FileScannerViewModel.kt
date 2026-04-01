package com.example.androidmaiden.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.FileRepository
import com.example.androidmaiden.model.*
import com.example.androidmaiden.utils.FileTypeUtils
import com.example.androidmaiden.views.eg.simFileNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Data class representing statistics for a folder's content.
 */
data class FolderAnalysisStats(
    val typeDistribution: Map<String, Long> = emptyMap(), // Type to total size
    val totalSize: Long = 0L,
    val fileCount: Int = 0,
    val folderCount: Int = 0
)

/**
 * ViewModel for the File System Analysis screen.
 * Handles navigation, file operations, and content analysis.
 */
class FileScannerViewModel(private val repository: FileRepository) : BaseViewModel() {

    private val defaultRoot = "/storage/emulated/0" // Public storage root
    private val mockRootPath = "mock_root"

    /**
     * The current directory node being displayed.
     */
    var currentDirectory by mutableStateOf<FileSysNode?>(null)
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
    private var realDataJob: kotlinx.coroutines.Job? = null

    init {
        // Initial load with default settings
        loadDirectory(if (useMock) mockRootPath else defaultRoot)
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
     * @param node The folder node to enter.
     */
    fun navigateTo(node: FileSysNode) {
        if (node.isFolder) {
            node.path?.let { targetPath ->
                // Fix issue 2: Prevent repeated stacking if the target is already the last item
                if (_pathStack.lastOrNull() != targetPath) {
                    _pathStack.add(targetPath)
                    loadDirectory(targetPath)
                }
            }
        }
    }

    /**
     * Navigates to a specific folder in the path stack (Breadcrumb click).
     */
    fun navigateToStackIndex(index: Int) {
        if (index >= 0 && index < _pathStack.size) {
            val targetPath = _pathStack[index]
            // Truncate the stack to the selected level
            while (_pathStack.size > index + 1) {
                _pathStack.removeAt(_pathStack.size - 1)
            }
            loadDirectory(targetPath)
        } else if (index == -1) {
            // Navigate to root
            _pathStack.clear()
            loadDirectory(if (useMock) mockRootPath else defaultRoot)
        }
    }

    /**
     * Navigates back to the parent directory.
     */
    fun navigateBack(): Boolean {
        if (_pathStack.size > 1) {
            _pathStack.removeAt(_pathStack.size - 1)
            val parentPath = _pathStack.last()
            loadDirectory(parentPath)
            return true
        } else if (_pathStack.size == 1) {
            _pathStack.clear()
            loadDirectory(if (useMock) mockRootPath else defaultRoot)
            return true
        }
        return false
    }

    /**
     * Loads a directory by path and triggers analysis.
     * @param path The absolute path to load.
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

    private fun loadMockDirectory(path: String) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                // Fix issue 1: Ensure result is correctly assigned on initial load
                val result = simFileNode()
                currentDirectory = result
                calculateStats(result.children)
                
                // Ensure breadcrumbs work for mock root too if it's the start
                if (_pathStack.isEmpty() && path != mockRootPath) {
                    _pathStack.add(path)
                }
            } catch (e: Exception) {
                _error.value = "Mock data error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun loadRealDirectory(path: String) {
        realDataJob = repository.getFilesByParent(path)
            .onEach { metadataList ->
                val node = mapMetadataToNode(path, metadataList)
                currentDirectory = node
                calculateStats(node.children)
                _isLoading.value = false
                if (_pathStack.isEmpty() && path == defaultRoot) {
                    _pathStack.add(path)
                }
            }
            .catch { e ->
                _error.value = "Database error: ${e.message}"
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }

    /**
     * Calculates statistics for the given list of nodes.
     * Fix issue 3: Accurate folder and file counting.
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
     * Deletes a file or folder.
     */
    fun deleteNode(node: FileSysNode) {
        if (useMock) return // Actions disabled in mock mode
        
        viewModelScope.launch {
            node.path?.let { path ->
                repository.deleteFile(path)
            }
        }
    }

    /**
     * Renames a file or folder.
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
     * Maps a list of [FileMetadata] from Room to the unified [FileSysNode] structure.
     */
    @OptIn(ExperimentalTime::class)
    private fun mapMetadataToNode(path: String, metadataList: List<FileMetadata>): FileSysNode {
        val children = metadataList.map { metadata ->
            FileSysNode(
                name = metadata.name,
                size = if (metadata.isDirectory) null else metadata.size,
                lastModified = metadata.lastModified,
                nodeType = if (metadata.isDirectory) NodeType.FOLDER else NodeType.FILE,
                folderType = if (metadata.isDirectory) FolderType.FOLDER else FolderType.OTHER,
                dataSource = DataSource.REAL,
                path = metadata.path,
                description = if (metadata.isDirectory) "" else "${metadata.size / 1024} KB"
            )
        }

        return FileSysNode(
            name = path.substringAfterLast("/", "Internal Storage"),
            nodeType = NodeType.FOLDER,
            folderType = FolderType.FOLDER,
            dataSource = DataSource.REAL,
            children = children,
            lastModified = Clock.System.now().toEpochMilliseconds(),
            description = "Path: $path",
            path = path
        )
    }
}
