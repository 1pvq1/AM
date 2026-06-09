package com.example.androidmaiden.data.network

import com.example.androidmaiden.util.DefaultHostResolver
import kotlin.test.Test
import kotlin.test.assertEquals

class LlmServiceTest {

    @Test
    fun testDefaultHostResolver() {
        val resolver = DefaultHostResolver()
        val url = "http://localhost:1234"
        assertEquals(url, resolver.resolve(url))
    }

    @Test
    fun testLanAddressHandling() {
        val resolver = DefaultHostResolver()
        val lanUrl = "http://192.168.1.50:1234"
        assertEquals(lanUrl, resolver.resolve(lanUrl))
    }
}
