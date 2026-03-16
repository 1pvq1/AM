package com.example.androidmaiden.views.eg

import com.example.androidmaiden.model.FileSysNode
import com.example.androidmaiden.model.FolderType
import com.example.androidmaiden.model.NodeType

// Simulated file structure
// Removed @Composable because this is called from the ViewModel/Background thread
fun simFileNode(): FileSysNode {
    return FileSysNode(
        name = "/",
        nodeType = NodeType.FOLDER,
        folderType = FolderType.FOLDER,
        description = "System Root",
        children = listOf(
            // User storage
            FileSysNode(
                "storage", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                description = "Internal Shared Storage",
                children = listOf(
                    FileSysNode(
                        "emulated", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                        description = "Device Storage Emulation",
                        children = listOf(
                            FileSysNode(
                                "0", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                                description = "Primary User Home",
                                children = listOf(
                                    FileSysNode(
                                        "Android",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.OTHER,
                                        description = "App Data & Cache",
                                        children = listOf(
                                            FileSysNode(
                                                "data",
                                                nodeType = NodeType.FOLDER,
                                                folderType = FolderType.OTHER,
                                                description = "Private App Data"
                                            ),
                                            FileSysNode(
                                                "media",
                                                nodeType = NodeType.FOLDER,
                                                folderType = FolderType.OTHER,
                                                description = "App Media Files"
                                            ),
                                            FileSysNode(
                                                "obb",
                                                nodeType = NodeType.FOLDER,
                                                folderType = FolderType.OTHER,
                                                description = "Expansion Files"
                                            )
                                        )
                                    ),
                                    FileSysNode(
                                        "Download",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.DOCUMENT,
                                        description = "Downloaded Files"
                                    ),
                                    FileSysNode(
                                        "Documents",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.DOCUMENT,
                                        description = "User Documents"
                                    ),
                                    FileSysNode(
                                        "DCIM",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.IMAGE,
                                        description = "Camera Photos & Videos"
                                    ),
                                    FileSysNode(
                                        "Pictures",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.IMAGE,
                                        description = "User Images"
                                    ),
                                    FileSysNode(
                                        "Screenshots",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.IMAGE,
                                        description = "Captured Screens"
                                    ),
                                    FileSysNode(
                                        "Movies",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.VIDEO,
                                        description = "User Videos"
                                    ),
                                    FileSysNode(
                                        "Music",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "Audio Library"
                                    ),
                                    FileSysNode(
                                        "Podcasts",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "Subscription Audio"
                                    ),
                                    FileSysNode(
                                        "Ringtones",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "Alert Tones"
                                    ),
                                    FileSysNode(
                                        "Alarms",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "Alarm Sounds"
                                    ),
                                    FileSysNode(
                                        "Notifications",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "Notification Sounds"
                                    ),
                                    FileSysNode(
                                        "Recordings",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "Voice Memos"
                                    )
                                )
                            )
                        )
                    ),
                    FileSysNode(
                        "{XXXX-XXXX}", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                        description = "External SD Card"
                    )
                )
            ),
            FileSysNode(
                "sdcard", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                description = "Legacy Symlink to Storage"
            ),

            // App/data
            FileSysNode(
                "data", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "System Data Partition",
                children = listOf(
                    FileSysNode(
                        "app", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "Installed Applications"
                    ),
                    FileSysNode(
                        "user", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "Multi-user Data"
                    ),
                    FileSysNode(
                        "user_de", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "Direct Boot Data"
                    ),
                    FileSysNode(
                        "media", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "Shared Media Data"
                    ),
                    FileSysNode(
                        "system", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "System Configuration"
                    )
                )
            ),

            // System partitions
            FileSysNode(
                "system", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Android System OS"
            ),
            FileSysNode(
                "system_ext", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "System Extensions"
            ),
            FileSysNode(
                "product", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Product Specific Files"
            ),
            FileSysNode(
                "vendor", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Hardware Vendor Files"
            ),
            FileSysNode(
                "odm", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Original Design Manufacturer Files"
            ),

            // APEX and config
            FileSysNode(
                "apex", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Modular System Components"
            ),
            FileSysNode(
                "linkerconfig", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Runtime Linker Config"
            ),
            FileSysNode(
                "etc", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Configuration Files"
            ),

            // Virtual/kernel
            FileSysNode(
                "proc", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Process Information"
            ),
            FileSysNode(
                "sys", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Kernel System Files"
            ),
            FileSysNode(
                "dev", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Device Nodes"
            ),

            // Mount/cache/metadata
            FileSysNode(
                "mnt", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Mount Points"
            ),
            FileSysNode(
                "cache", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Temporary Cache"
            ),
            FileSysNode(
                "metadata", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Encrypted Metadata"
            ),

            // Optional/vendor-specific
            FileSysNode(
                "persist", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Persistent Vendor Data"
            ),
            FileSysNode(
                "oem", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "OEM Customizations"
            ),
            FileSysNode(
                "config", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "Storage Config"
            )
        )
    )
}
