package com.example.androidmaiden.presentation.ui.screens.settings.llm.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.stringResource

/**
 * Composable for entering the API key.
 */
@Composable
fun ApiKeySetting(apiKey: String, onApiKeyChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = apiKey,
        onValueChange = onApiKeyChange,
        label = { Text(stringResource(id = "settings_llm_api_key")) },
        placeholder = { Text(stringResource(id = "settings_llm_api_key_placeholder")) },
        leadingIcon = {
            Icon(
                Icons.Default.Key,
                contentDescription = stringResource(id = "settings_llm_api_key")
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
