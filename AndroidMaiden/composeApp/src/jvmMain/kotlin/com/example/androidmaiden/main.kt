package com.example.androidmaiden

import androidmaiden.composeapp.generated.resources.Res
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

//import org.jetbrains.compose.resources.painterResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AndroidMaiden",
//        icon = painterResource(Res.drawable.am_app_icon) // Sets the runtime icon. top-left corner of the app window and on the taskbar while the app is running
//        icon = painterResource("src/commonMain/composeResources/drawable/am_app_icon.png")
    ) {
        App()
    }
}