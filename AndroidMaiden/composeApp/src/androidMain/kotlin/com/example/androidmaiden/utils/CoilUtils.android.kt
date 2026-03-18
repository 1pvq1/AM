package com.example.androidmaiden.utils

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.video.VideoFrameDecoder

/**
 * Android implementation of the ImageLoader with Video support.
 */
actual fun getAsyncImageLoader(context: PlatformContext): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            add(VideoFrameDecoder.Factory())
        }
        .build()
}
