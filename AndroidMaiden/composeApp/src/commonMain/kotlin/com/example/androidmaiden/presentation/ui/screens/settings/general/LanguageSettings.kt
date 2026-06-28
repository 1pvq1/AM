package com.example.androidmaiden.presentation.ui.screens.settings.general

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.*
import com.example.androidmaiden.presentation.ui.screens.settings.Language
import com.example.androidmaiden.presentation.ui.components.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Preview for the Language settings group.
 */
@Preview
@Composable
fun PreviewLanguageSettingsGroup() {
    LanguageSettingsGroup(
        language = Language.FOLLOW_SYSTEM,
        onLanguageChange = {}
    )
}

/**
 * A group of settings for changing the application language.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsGroup(
    language: Language,
    onLanguageChange: (Language) -> Unit
) {
    val languageOptions = remember { Language.entries }
    var expanded by remember { mutableStateOf(false) }
    val selectedLanguageText = language.getDisplayName()

    SettingsGroup(stringResource(id = "settings_language_title")) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedLanguageText,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(id = "settings_language_title")) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Language,
                            contentDescription = stringResource(id = "settings_language_title")
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    languageOptions.forEachIndexed { index, lang ->
                        DropdownMenuItem(
                            text = { Text(lang.getDisplayName()) },
                            onClick = {
                                onLanguageChange(lang)
                                expanded = false
                            }
                        )
                        if (index == 0) { // Add a divider after the "System" option
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
