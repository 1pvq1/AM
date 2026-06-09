package com.example.androidmaiden.platform

import android.os.Build
import com.example.androidmaiden.util.HostResolver

/**
 * Android implementation of HostResolver.
 * Automatically maps 'localhost' and '127.0.0.1' to '10.0.2.2' when running on an emulator.
 */
class AndroidHostResolver : HostResolver {
    
    private val isEmulator: Boolean by lazy {
        (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
    }

    override fun resolve(url: String): String {
        if (!isEmulator) return url

        return url.replace("localhost", "10.0.2.2")
                  .replace("127.0.0.1", "10.0.2.2")
    }
}
