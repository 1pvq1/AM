package com.example.androidmaiden.utils

import coil3.ImageLoader
import coil3.PlatformContext

/**
 * Expected function to provide a platform-specific ImageLoader.
 * This will allow us to inject the VideoFrameDecoder on Android.
 */
expect fun getAsyncImageLoader(context: PlatformContext): ImageLoader
