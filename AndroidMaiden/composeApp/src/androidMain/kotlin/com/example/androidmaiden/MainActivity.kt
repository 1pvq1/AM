package com.example.androidmaiden

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidmaiden.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize Koin BEFORE setContent so ViewModels can be injected immediately
        initKoin(this)

        setContent {
            App()
        }
    }
}

private fun initKoin(context: Context) {
    // Check if Koin is already started to prevent crashes on recreation
    if (GlobalContext.getKoinApplicationOrNull() == null) {
        startKoin {
            androidContext(context)
            modules(commonModule, platformModule)
        }
    }
}


@Preview
@Composable
fun AppAndroidPreview() {
    App()
}


