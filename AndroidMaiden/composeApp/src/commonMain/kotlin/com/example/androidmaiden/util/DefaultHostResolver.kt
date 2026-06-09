package com.example.androidmaiden.util

/**
 * Default implementation of HostResolver that returns the URL unchanged.
 */
class DefaultHostResolver : HostResolver {
    override fun resolve(url: String): String = url
}
