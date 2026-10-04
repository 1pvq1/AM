package com.example.androidmaiden.domain.service

/**
 * Domain service providing educational architectural explanations for standard Android file system directories.
 * Helps users and developers understand Android 13+ storage layout, permissions, and system partitions.
 */
object AndroidFolderExplainer {

    /**
     * Map of standard folder paths or names to their architectural descriptions.
     */
    private val folderExplanations = mapOf(
        "/" to "System Root: Top-level mount point containing Virtual FS, System Partitions, and Storage mounts.",
        "storage" to "Internal Shared Storage: Root mount point for internal and external storage partitions.",
        "emulated" to "Device Storage Emulation: FUSE/sdcardfs virtual layer isolating user storage space.",
        "0" to "Primary User Home: Primary user (User 0) internal storage space under Scoped Storage.",
        "Android" to "App Data & Cache: Parent directory for private application storage managed by Android OS.",
        "data" to "Private App Data: Private app storage accessible only by respective owner apps under Scoped Storage.",
        "media" to "App Media Files: Application media assets shared across system media services.",
        "obb" to "Expansion Files: OBB expansion assets for large applications and games.",
        "Download" to "Downloaded Files: Public downloads directory for user-saved files.",
        "Documents" to "User Documents: Standard directory for user documents and text files.",
        "DCIM" to "Digital Camera Images: Standard photo/video location for camera applications.",
        "Pictures" to "User Images: Saved pictures, screenshots, and photo editing outputs.",
        "Screenshots" to "Captured Screens: System and user captured screen images.",
        "Movies" to "User Videos: Video recordings, downloaded clips, and movies.",
        "Music" to "Audio Library: Music files and audio tracks.",
        "Podcasts" to "Podcasts: Downloaded podcast episodes and audio series.",
        "Ringtones" to "Ringtones: Custom incoming phone call ringtones.",
        "Alarms" to "Alarm Sounds: Clock alarm alert sound files.",
        "Notifications" to "Notification Sounds: System notification alert tones.",
        "Recordings" to "Voice Memos: Voice recorder output and audio memos.",
        "app" to "Installed Applications: APK binaries and native libraries for installed user apps.",
        "user" to "Multi-user Data: Isolated data containers for secondary device user accounts.",
        "user_de" to "Direct Boot Data: Device-encrypted storage accessible before user credentials are unlocked.",
        "system" to "Android System OS: Core framework libraries, system binaries, and system apps.",
        "system_ext" to "System Extensions: OEM/carrier extended framework services and custom components.",
        "product" to "Product Configurations: Device product-specific apps, configurations, and themes.",
        "vendor" to "Hardware Vendor Partition: Hardware Abstraction Layer (HAL) libraries and vendor binaries (Project Treble).",
        "odm" to "ODM Board Customizations: Original Design Manufacturer board-specific hardware drivers.",
        "apex" to "Modular System Components: APEX containers updated independently via Google Play Mainline.",
        "linkerconfig" to "Runtime Linker Config: Dynamic linker runtime namespace mapping auto-generated at boot.",
        "etc" to "Configuration Files: System-wide configuration files, permissions, and network settings.",
        "proc" to "Process Pseudo-Filesystem: Kernel virtual filesystem providing runtime process and hardware stats.",
        "sys" to "Sysfs Pseudo-Filesystem: Kernel virtual filesystem exposing hardware device hierarchy and power controls.",
        "dev" to "Device Nodes: Character and block special device driver interface nodes.",
        "mnt" to "Mount Points: Dynamic mount locations for external SD cards, USB drives, and temp storage.",
        "cache" to "Temporary Cache: Legacy partition for system updates and temporary recovery files.",
        "metadata" to "Encrypted Metadata: File-Based Encryption (FBE) metadata and key storage.",
        "persist" to "Persistent Vendor Data: Factory calibration, Wi-Fi MACs, and hardware calibration parameters.",
        "oem" to "OEM Customizations: Manufacturer specific preloaded assets and carrier configs."
    )

    /**
     * Resolves an architectural explanation for a given folder path or folder name.
     *
     * @param path The absolute or relative path of the directory.
     * @param folderName The name of the directory.
     * @return A descriptive educational explanation of the folder's role in Android architecture.
     */
    fun explainFolder(path: String?, folderName: String): String {
        if (path != null) {
            val cleanPath = path.trimEnd('/')
            folderExplanations[cleanPath]?.let { return it }

            // Match sub-paths like Android/data or storage/emulated/0
            when {
                cleanPath.endsWith("/Android/data") -> return "Private App Data: Isolated application storage under Scoped Storage rules."
                cleanPath.endsWith("/Android/media") -> return "App Media Files: Shared media files managed by specific installed apps."
                cleanPath.endsWith("/Android/obb") -> return "App Expansion Files: High-capacity game and app asset files."
                cleanPath.contains("/storage/emulated/0") -> {
                    val subFolder = cleanPath.substringAfterLast("/")
                    folderExplanations[subFolder]?.let { return it }
                }
            }
        }

        // Fallback to matching by folder name
        return folderExplanations[folderName] ?: "User/System Directory: Standard file system folder."
    }
}
