package com.example.androidmaiden

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.androidmaiden.di.commonModule
import com.example.androidmaiden.di.platformModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(commonModule, platformModule)
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "AndroidMaiden",
        ) {
            App()
        }
    }
}
