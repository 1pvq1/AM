package com.example.androidmaiden

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.androidmaiden.domain.model.Screen
import com.example.androidmaiden.presentation.ui.components.AppNavigationBar
import com.example.androidmaiden.presentation.ui.screens.*
import com.example.androidmaiden.presentation.ui.screens.fileSystem.dashboard.FilesScreen
import com.example.androidmaiden.presentation.ui.theme.AppTheme
import com.example.androidmaiden.presentation.viewmodel.TodoViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.example.androidmaiden.presentation.ui.screens.fileSystem.analyze.FileAnalysisScreen
import com.example.androidmaiden.presentation.ui.screens.fileSystem.classify.FileClassifyPage
import com.example.androidmaiden.presentation.ui.screens.fileSystem.clean.FileCleanPage
import com.example.androidmaiden.presentation.ui.screens.fileSystem.organize.FileOrganizePage
import com.example.androidmaiden.presentation.ui.screens.home.HomeScreen
import com.example.androidmaiden.presentation.ui.screens.interaction.components.RegularChatViewPreview
import com.example.androidmaiden.presentation.ui.screens.interaction.components.VirtualChatViewPreview
import com.example.androidmaiden.presentation.ui.screens.settings.SettingsScreenPreview
import com.example.androidmaiden.presentation.ui.screens.settings.llm.AdvancedLlmSettingsPage
import com.example.androidmaiden.presentation.ui.screens.skills.SkillsScreen
import com.example.androidmaiden.presentation.ui.screens.todo.TodoScreen
import org.koin.compose.KoinContext
import kotlin.time.ExperimentalTime

/**
 * A helper wrapper to preview screens within the application's standard layout.
 * This helps in harmonizing UI detection and design consistency.
 */
@Composable
fun AppPreviewWrapper(
    currentScreen: Screen,
    content: @Composable () -> Unit
) {
    AppTheme {
        Scaffold(
            bottomBar = {
                AppNavigationBar(
                    currentScreen = currentScreen,
                    onScreenSelected = {}
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun FullAppHomePreview() {
    AppPreviewWrapper(Screen.Home) {
        HomeScreen()
    }
}

@Preview
@Composable
fun FullAppSkillsPreview() {
    AppPreviewWrapper(Screen.Skills) {
        SkillsScreen(onNavigate = {})
    }
}

@Preview
@Composable
fun FullAppFilesPreview() {
    AppPreviewWrapper(Screen.Files) {
        FilesScreen(onNavigate = {})
    }
}

@Preview
@Composable
fun FullAppSettingsPreview() {
    AppPreviewWrapper(Screen.Settings) {
        SettingsScreenPreview()
    }
}

@Preview
@Composable
fun FullAppTodoPreview() {
    AppPreviewWrapper(Screen.Todo) {
        TodoScreen(viewModel = TodoViewModel())
    }
}

@Preview
@Composable
fun FullAppRegularChatViewPreview() {
    AppPreviewWrapper(Screen.Settings) {
        RegularChatViewPreview()
    }
}

@Preview
@Composable
fun FullAppVirtualChatViewPreview() {
    AppPreviewWrapper(Screen.CharacterInteraction) {
        VirtualChatViewPreview()
    }
}

@Preview
@Composable
fun FullAppFileAnalysisPreview() {
    AppPreviewWrapper(Screen.FileAnalysis) {
        KoinContext {
            FileAnalysisScreen(onNavigateUp = {})
        }
    }
}



@Preview
@Composable
fun FullAppAdvancedLlmSettingsPreview() {
    AppPreviewWrapper(Screen.AdvancedLlmSettings) {
        KoinContext {
            AdvancedLlmSettingsPage(onNavigateBack = {})
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun FullAppFileClassifyPreview() {
    AppPreviewWrapper(Screen.FileClassify) {
        KoinContext {
            FileClassifyPage(onBack = {})
        }
    }
}

@Preview
@Composable
fun FullAppFileOrganizePreview() {
    AppPreviewWrapper(Screen.FileOrganize) {
        KoinContext {
            FileOrganizePage(onBack = {})
        }
    }
}

@Preview
@Composable
fun FullAppFileCleanPreview() {
    AppPreviewWrapper(Screen.FileClean) {
        KoinContext {
            FileCleanPage(onBack = {})
        }
    }
}
