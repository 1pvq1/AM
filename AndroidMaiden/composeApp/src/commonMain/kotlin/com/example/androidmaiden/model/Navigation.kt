package com.example.androidmaiden.model

// Define dst for navigator
sealed class Screen(val title: String) {
    data object Home : Screen("Home")
    data object Skills : Screen("Skills")
    data object Settings : Screen("Settings")
    data object Files : Screen("File Management")
    data object FileAnalysis : Screen("File Analysis")
    data object FileClassify : Screen("File Classify")
    data object FileOrganize : Screen("File Organize")
    data object FileClean : Screen("File Cleanup")
    data object Todo : Screen("Todo Lists")
    data object CharacterInteraction : Screen("Chat with AI")    
    data object AdvancedLlmSettings : Screen("Advanced LLM Settings")
}
