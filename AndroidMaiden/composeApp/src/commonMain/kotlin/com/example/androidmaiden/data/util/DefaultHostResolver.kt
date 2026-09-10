package com.example.androidmaiden.data.util

import com.example.androidmaiden.domain.service.HostResolver

/**
 * Default implementation of HostResolver that returns the URL unchanged.
 */
class DefaultHostResolver : HostResolver {
    override fun resolve(address: String): String = address
}
