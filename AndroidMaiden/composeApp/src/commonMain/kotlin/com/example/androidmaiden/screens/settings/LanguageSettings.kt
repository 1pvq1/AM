package com.example.androidmaiden.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.screens.Language
import com.example.androidmaiden.screens.SettingsGroup
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PreviewLanguageSettingsGroup() {
    LanguageSettingsGroup(
        language = Language.FOLLOW_SYSTEM,
        onLanguageChange = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSettingsGroup(
    language: Language,
    onLanguageChange: (Language) -> Unit
) {
    val options = listOf(
        Language.FOLLOW_SYSTEM to "System",
        Language.ENGLISH to "English",
        Language.CHINESE to "Chinese"
    )

    SettingsGroup("Language") {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Language,
                    contentDescription = "Language",
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text("Language", style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(12.dp))
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                options.forEachIndexed { index, (lang, label) ->
                    SegmentedButton(
                        selected = language == lang,
                        onClick = { onLanguageChange(lang) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size)
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}
