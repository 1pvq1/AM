package com.example.androidmaiden.presentation.ui.features.eg

import com.example.androidmaiden.domain.model.FileSysNode
import com.example.androidmaiden.domain.model.FolderType
import com.example.androidmaiden.domain.model.NodeType
import com.example.androidmaiden.domain.service.AndroidFolderExplainer

/**
 * Returns a simulated Android 13 file system tree hierarchy.
 * Used for educational analysis and UI demonstration of Android OS directory architecture.
 */
fun simFileNode(): FileSysNode {
    return FileSysNode(
        name = "/",
        path = "/",
        nodeType = NodeType.FOLDER,
        folderType = FolderType.FOLDER,
        description = AndroidFolderExplainer.explainFolder("/", "/"),
        children = listOf(
            // Storage subtree
            FileSysNode(
                name = "storage",
                path = "/storage",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.FOLDER,
                description = AndroidFolderExplainer.explainFolder("/storage", "storage"),
                children = listOf(
                    FileSysNode(
                        name = "emulated",
                        path = "/storage/emulated",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.FOLDER,
                        description = AndroidFolderExplainer.explainFolder("/storage/emulated", "emulated"),
                        children = listOf(
                            FileSysNode(
                                name = "0",
                                path = "/storage/emulated/0",
                                nodeType = NodeType.FOLDER,
                                folderType = FolderType.FOLDER,
                                description = AndroidFolderExplainer.explainFolder("/storage/emulated/0", "0"),
                                children = listOf(
                                    FileSysNode(
                                        name = "Android",
                                        path = "/storage/emulated/0/Android",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.OTHER,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Android", "Android"),
                                        children = listOf(
                                            FileSysNode(
                                                name = "data",
                                                path = "/storage/emulated/0/Android/data",
                                                nodeType = NodeType.FOLDER,
                                                folderType = FolderType.OTHER,
                                                description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Android/data", "data")
                                            ),
                                            FileSysNode(
                                                name = "media",
                                                path = "/storage/emulated/0/Android/media",
                                                nodeType = NodeType.FOLDER,
                                                folderType = FolderType.OTHER,
                                                description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Android/media", "media")
                                            ),
                                            FileSysNode(
                                                name = "obb",
                                                path = "/storage/emulated/0/Android/obb",
                                                nodeType = NodeType.FOLDER,
                                                folderType = FolderType.OTHER,
                                                description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Android/obb", "obb")
                                            )
                                        )
                                    ),
                                    FileSysNode(
                                        name = "Download",
                                        path = "/storage/emulated/0/Download",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.DOCUMENT,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Download", "Download")
                                    ),
                                    FileSysNode(
                                        name = "Documents",
                                        path = "/storage/emulated/0/Documents",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.DOCUMENT,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Documents", "Documents")
                                    ),
                                    FileSysNode(
                                        name = "DCIM",
                                        path = "/storage/emulated/0/DCIM",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.IMAGE,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/DCIM", "DCIM")
                                    ),
                                    FileSysNode(
                                        name = "Pictures",
                                        path = "/storage/emulated/0/Pictures",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.IMAGE,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Pictures", "Pictures")
                                    ),
                                    FileSysNode(
                                        name = "Screenshots",
                                        path = "/storage/emulated/0/Pictures/Screenshots",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.IMAGE,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Pictures/Screenshots", "Screenshots")
                                    ),
                                    FileSysNode(
                                        name = "Movies",
                                        path = "/storage/emulated/0/Movies",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.VIDEO,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Movies", "Movies")
                                    ),
                                    FileSysNode(
                                        name = "Music",
                                        path = "/storage/emulated/0/Music",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Music", "Music")
                                    ),
                                    FileSysNode(
                                        name = "Podcasts",
                                        path = "/storage/emulated/0/Podcasts",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Podcasts", "Podcasts")
                                    ),
                                    FileSysNode(
                                        name = "Ringtones",
                                        path = "/storage/emulated/0/Ringtones",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Ringtones", "Ringtones")
                                    ),
                                    FileSysNode(
                                        name = "Alarms",
                                        path = "/storage/emulated/0/Alarms",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Alarms", "Alarms")
                                    ),
                                    FileSysNode(
                                        name = "Notifications",
                                        path = "/storage/emulated/0/Notifications",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Notifications", "Notifications")
                                    ),
                                    FileSysNode(
                                        name = "Recordings",
                                        path = "/storage/emulated/0/Recordings",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = AndroidFolderExplainer.explainFolder("/storage/emulated/0/Recordings", "Recordings")
                                    )
                                )
                            )
                        )
                    ),
                    FileSysNode(
                        name = "{XXXX-XXXX}",
                        path = "/storage/{XXXX-XXXX}",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.FOLDER,
                        description = "External SD Card storage mount point."
                    )
                )
            ),
            FileSysNode(
                name = "sdcard",
                path = "/sdcard",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.FOLDER,
                description = "Legacy symlink pointing to /storage/emulated/0."
            ),

            // Data partition subtree
            FileSysNode(
                name = "data",
                path = "/data",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/data", "data"),
                children = listOf(
                    FileSysNode(
                        name = "app",
                        path = "/data/app",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.OTHER,
                        description = AndroidFolderExplainer.explainFolder("/data/app", "app")
                    ),
                    FileSysNode(
                        name = "user",
                        path = "/data/user",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.OTHER,
                        description = AndroidFolderExplainer.explainFolder("/data/user", "user")
                    ),
                    FileSysNode(
                        name = "user_de",
                        path = "/data/user_de",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.OTHER,
                        description = AndroidFolderExplainer.explainFolder("/data/user_de", "user_de")
                    ),
                    FileSysNode(
                        name = "media",
                        path = "/data/media",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.OTHER,
                        description = "Shared Media Data storage backing FUSE layer."
                    ),
                    FileSysNode(
                        name = "system",
                        path = "/data/system",
                        nodeType = NodeType.FOLDER,
                        folderType = FolderType.OTHER,
                        description = "System configuration settings, usage stats, and packages.xml."
                    )
                )
            ),

            // System OS partitions
            FileSysNode(
                name = "system",
                path = "/system",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/system", "system")
            ),
            FileSysNode(
                name = "system_ext",
                path = "/system_ext",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/system_ext", "system_ext")
            ),
            FileSysNode(
                name = "product",
                path = "/product",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/product", "product")
            ),
            FileSysNode(
                name = "vendor",
                path = "/vendor",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/vendor", "vendor")
            ),
            FileSysNode(
                name = "odm",
                path = "/odm",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/odm", "odm")
            ),

            // APEX and config
            FileSysNode(
                name = "apex",
                path = "/apex",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/apex", "apex")
            ),
            FileSysNode(
                name = "linkerconfig",
                path = "/linkerconfig",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/linkerconfig", "linkerconfig")
            ),
            FileSysNode(
                name = "etc",
                path = "/etc",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/etc", "etc")
            ),

            // Kernel and Virtual FS
            FileSysNode(
                name = "proc",
                path = "/proc",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/proc", "proc")
            ),
            FileSysNode(
                name = "sys",
                path = "/sys",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/sys", "sys")
            ),
            FileSysNode(
                name = "dev",
                path = "/dev",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/dev", "dev")
            ),

            // Mount & Cache
            FileSysNode(
                name = "mnt",
                path = "/mnt",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/mnt", "mnt")
            ),
            FileSysNode(
                name = "cache",
                path = "/cache",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/cache", "cache")
            ),
            FileSysNode(
                name = "metadata",
                path = "/metadata",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/metadata", "metadata")
            ),

            // Vendor/OEM
            FileSysNode(
                name = "persist",
                path = "/persist",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/persist", "persist")
            ),
            FileSysNode(
                name = "oem",
                path = "/oem",
                nodeType = NodeType.FOLDER,
                folderType = FolderType.OTHER,
                description = AndroidFolderExplainer.explainFolder("/oem", "oem")
            )
        )
    )
}
