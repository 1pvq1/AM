package com.example.androidmaiden.presentation.ui.features.others

import androidmaiden.composeapp.generated.resources.Res
import androidmaiden.composeapp.generated.resources.compose_multiplatform
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidmaiden.Greeting
import com.example.androidmaiden.platform.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Example composable for testing UI components.
 */
@Preview
@Composable
private fun TestingEg() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = "home_testing_area"),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = { showContent = !showContent }) {
                    Text(stringResource(id = "home_click_me"))
                }
                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(painterResource(Res.drawable.compose_multiplatform), null)
                        Spacer(Modifier.height(16.dp))
                        Text("Compose: $greeting")
                    }
                }
            }
        }
    }
}
