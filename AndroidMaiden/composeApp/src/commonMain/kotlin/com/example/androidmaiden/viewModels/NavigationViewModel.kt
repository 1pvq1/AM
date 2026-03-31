package com.example.androidmaiden.viewModels

import com.example.androidmaiden.model.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : BaseViewModel() {
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _isNavigationBarVisible = MutableStateFlow(true)
    val isNavigationBarVisible: StateFlow<Boolean> = _isNavigationBarVisible.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        _isNavigationBarVisible.value = true
    }

    fun setNavigationBarVisible(visible: Boolean) {
        _isNavigationBarVisible.value = visible
    }
}
