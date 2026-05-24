package com.example.androidmaiden.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.local.FileMetadata
import com.example.androidmaiden.data.repository.FileRepository
import com.example.androidmaiden.data.local.Tag
import com.example.androidmaiden.data.local.FileWithTags
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the file organization features (tagging and searching).
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class FileOrganizeViewModel(
    private val repository: FileRepository
) : ViewModel() {

    /**
     * Flow of all available tags.
     */
    val allTags: StateFlow<List<Tag>> = repository.getAllTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently selected tag to filter files
    private val _selectedTag = MutableStateFlow<Tag?>(null)

    /**
     * The currently selected tag for filtering.
     */
    val selectedTag: StateFlow<Tag?> = _selectedTag.asStateFlow()

    /**
     * Flow of files that have the currently selected tag.
     */
    val taggedFiles: StateFlow<List<FileMetadata>> = _selectedTag
        .flatMapLatest { tag ->
            if (tag == null) {
                flowOf(emptyList())
            } else {
                repository.getTagWithFiles(tag.id).map { it?.files ?: emptyList() }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search query for untagged files or general search
    private val _searchQuery = MutableStateFlow("")

    /**
     * The current search query string.
     */
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // --- Navigation State ---
    private val _currentPath = MutableStateFlow<String>("") // Empty string for root

    /**
     * The current path within the file system being browsed.
     */
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    /**
     * A stack of path segments for breadcrumb navigation.
     */
    val pathStack: StateFlow<List<String>> = _currentPath.map { path ->
        if (path.isEmpty()) emptyList() else path.split("/").filter { it.isNotEmpty() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Flow of files in the current directory.
     */
    val currentDirectoryFiles: StateFlow<List<FileMetadata>> = _currentPath
        .flatMapLatest { path ->
            repository.getFilesByParent(path)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Navigates to a specific directory path.
     */
    fun navigateTo(path: String) {
        _currentPath.value = path
    }

    /**
     * Navigates up to the parent directory.
     */
    fun navigateUp() {
        val current = _currentPath.value
        if (current.isNotEmpty()) {
            val parent = current.substringBeforeLast("/", "")
            _currentPath.value = parent
        }
    }

    /**
     * Navigates to a specific index in the current path stack.
     */
    fun navigateToIndex(index: Int) {
        val stack = pathStack.value
        if (index < stack.size) {
            val newPath = stack.take(index + 1).joinToString("/", prefix = "/")
            _currentPath.value = newPath
        }
    }

    /**
     * Navigates to the root directory.
     */
    fun navigateToRoot() {
        _currentPath.value = ""
    }

    /**
     * Flow of file search results based on the current query.
     */
    val searchResults: StateFlow<List<FileMetadata>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.length < 2) flowOf(emptyList())
            else repository.searchFiles(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Selects a tag for filtering files.
     */
    fun selectTag(tag: Tag?) {
        _selectedTag.value = tag
    }

    /**
     * Updates the search query string.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Creates a new tag with the given name and color.
     */
    fun createTag(name: String, colorHex: String) {
        viewModelScope.launch {
            repository.addTag(name, colorHex)
        }
    }

    /**
     * Deletes an existing tag.
     */
    fun deleteTag(tag: Tag) {
        viewModelScope.launch {
            if (_selectedTag.value == tag) _selectedTag.value = null
            repository.removeTag(tag)
        }
    }

    /**
     * Updates an existing tag's details.
     */
    fun updateTag(tag: Tag) {
        viewModelScope.launch {
            repository.updateTag(tag)
        }
    }

    /**
     * Associates a tag with a specific file path.
     */
    fun addTagToFile(path: String, tag: Tag) {
        viewModelScope.launch {
            repository.addTagToFile(path, tag.id)
        }
    }

    /**
     * Removes a tag association from a specific file path.
     */
    fun removeTagFromFile(path: String, tag: Tag) {
        viewModelScope.launch {
            repository.removeTagFromFile(path, tag.id)
        }
    }

    /**
     * Returns a flow of a file and its associated tags.
     */
    fun getFileWithTags(path: String): Flow<FileWithTags?> {
        return repository.getFileWithTags(path)
    }
}
