package com.example.androidmaiden.domain.service

/**
 * Platform-independent interface for resolving hostnames or IP addresses.
 */
interface HostResolver {
    /**
     * Resolves the given [address] to a platform-specific address string.
     * Helpful for handling localhost variations between emulators and host machines.
     */
    fun resolve(address: String): String
}
