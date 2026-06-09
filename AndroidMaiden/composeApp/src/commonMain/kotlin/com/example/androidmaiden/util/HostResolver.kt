package com.example.androidmaiden.util

/**
 * Interface for resolving host addresses based on the current platform environment.
 * Particularly useful for handling Android emulator aliases (10.0.2.2).
 */
interface HostResolver {
    /**
     * Resolves the given URL/address to a platform-appropriate value.
     */
    fun resolve(url: String): String
}
