package com.example.androidmaiden.presentation.ui.screens.settings.llm.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.platform.stringResource

/**
 * Composable for configuring the local LLM address.
 */
@Composable
fun LocalLlmAddressSetting(
    address: String,
    onAddressChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = address,
        onValueChange = onAddressChange,
        label = { Text(stringResource(id = "settings_advanced_llm_local_address_label")) },
        placeholder = { Text("http://192.168.1.x:1234/v1") },
        leadingIcon = { Icon(Icons.Default.Computer, contentDescription = null) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Uri,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
