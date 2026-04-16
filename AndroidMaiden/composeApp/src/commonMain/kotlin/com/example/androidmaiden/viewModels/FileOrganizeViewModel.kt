package com.example.androidmaiden.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmaiden.data.FileMetadata
import com.example.androidmaiden.data.FileRepository
import com.example.androidmaiden.data.Tag
import com.example.androidmaiden.data.FileWithTags
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class FileOrganizeViewModel(
    private val repository: FileRepository
) : ViewModel() {

    // All available tags
    val allTags: StateFlow<List<Tag>> = repository.getAllTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently selected tag to filter files
    private val _selectedTag = MutableStateFlow<Tag?>(null)
    val selectedTag: StateFlow<Tag?> = _selectedTag.asStateFlow()

    // Files filtered by the selected tag
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
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // --- Navigation State ---
    private val _currentPath = MutableStateFlow<String>("") // Empty string for root
    val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    val pathStack: StateFlow<List<String>> = _currentPath.map { path ->
        if (path.isEmpty()) emptyList() else path.split("/").filter { it.isNotEmpty() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Files in current directory
    val currentDirectoryFiles: StateFlow<List<FileMetadata>> = _currentPath
        .flatMapLatest { path ->
            repository.getFilesByParent(path)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateTo(path: String) {
        _currentPath.value = path
    }

    fun navigateUp() {
        val current = _currentPath.value
        if (current.isNotEmpty()) {
            val parent = current.substringBeforeLast("/", "")
            _currentPath.value = parent
        }
    }

    fun navigateToIndex(index: Int) {
        val stack = pathStack.value
        if (index < stack.size) {
            val newPath = stack.take(index + 1).joinToString("/", prefix = "/")
            _currentPath.value = newPath
        }
    }

    fun navigateToRoot() {
        _currentPath.value = ""
    }

    // Search results for adding tags
    val searchResults: StateFlow<List<FileMetadata>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.length < 2) flowOf(emptyList())
            else repository.searchFiles(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectTag(tag: Tag?) {
        _selectedTag.value = tag
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun createTag(name: String, colorHex: String) {
        viewModelScope.launch {
            repository.addTag(name, colorHex)
        }
    }

    fun deleteTag(tag: Tag) {
        viewModelScope.launch {
            if (_selectedTag.value == tag) _selectedTag.value = null
            repository.removeTag(tag)
        }
    }

    fun updateTag(tag: Tag) {
        viewModelScope.launch {
            repository.updateTag(tag)
        }
    }

    fun addTagToFile(path: String, tag: Tag) {
        viewModelScope.launch {
            repository.addTagToFile(path, tag.id)
        }
    }

    fun removeTagFromFile(path: String, tag: Tag) {
        viewModelScope.launch {
            repository.removeTagFromFile(path, tag.id)
        }
    }

    fun getFileWithTags(path: String): Flow<FileWithTags?> {
        return repository.getFileWithTags(path)
    }
}
