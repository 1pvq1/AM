package com.example.androidmaiden

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform