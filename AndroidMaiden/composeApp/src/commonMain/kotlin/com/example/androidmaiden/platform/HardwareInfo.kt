package com.example.androidmaiden.platform

import com.example.androidmaiden.domain.hardware.model.*

/**
 * Platform-specific expect declaration to fetch current OS hardware details.
 */
expect fun getPlatformSystemResources(context: Any? = null): SystemResources
