package com.example.androidmaiden.views.eg

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Videocam
import com.example.androidmaiden.model.FileNode
import com.example.androidmaiden.model.FolderType
import com.example.androidmaiden.model.NodeType
import com.example.androidmaiden.screens.pages.FileCategory

// 模拟文件结构
fun simFileNode(): FileNode {
    return FileNode(
        name = "/",
        nodeType = NodeType.FOLDER,
        folderType = FolderType.FOLDER,
        description = "Android 12 文件系统根目录",
        children = listOf(
            // User storage
            FileNode(
                "storage", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                description = "通用存储入口（内部/外部/挂载卷）",
                children = listOf(
                    FileNode(
                        "emulated", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                        description = "内部存储的模拟挂载点",
                        children = listOf(
                            FileNode(
                                "0", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                                description = "用户主目录（/sdcard 指向此处）",
                                children = listOf(
                                    FileNode(
                                        "Android",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.OTHER,
                                        description = "应用数据目录（data、media、obb）",
                                        children = listOf(
                                            FileNode(
                                                "data",
                                                nodeType = NodeType.FOLDER,
                                                folderType = FolderType.OTHER,
                                                description = "应用私有数据（受沙盒与权限限制）"
                                            ),
                                            FileNode(
                                                "media",
                                                nodeType = NodeType.FOLDER,
                                                folderType = FolderType.OTHER,
                                                description = "应用媒体数据（面向索引）"
                                            ),
                                            FileNode(
                                                "obb",
                                                nodeType = NodeType.FOLDER,
                                                folderType = FolderType.OTHER,
                                                description = "大型资源包（游戏等）"
                                            )
                                        )
                                    ),
                                    FileNode(
                                        "Download",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.DOCUMENT,
                                        description = "下载文件"
                                    ),
                                    FileNode(
                                        "Documents",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.DOCUMENT,
                                        description = "文档文件"
                                    ),
                                    FileNode(
                                        "DCIM",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.IMAGE,
                                        description = "相机拍摄内容"
                                    ),
                                    FileNode(
                                        "Pictures",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.IMAGE,
                                        description = "图片与截图"
                                    ),
                                    FileNode(
                                        "Screenshots",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.IMAGE,
                                        description = "截图（部分设备在 Pictures/Screenshots）"
                                    ),
                                    FileNode(
                                        "Movies",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.VIDEO,
                                        description = "视频文件"
                                    ),
                                    FileNode(
                                        "Music",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "音乐文件"
                                    ),
                                    FileNode(
                                        "Podcasts",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "播客音频"
                                    ),
                                    FileNode(
                                        "Ringtones",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "铃声"
                                    ),
                                    FileNode(
                                        "Alarms",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "闹钟音"
                                    ),
                                    FileNode(
                                        "Notifications",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "通知音"
                                    ),
                                    FileNode(
                                        "Recordings",
                                        nodeType = NodeType.FOLDER,
                                        folderType = FolderType.MUSIC,
                                        description = "录音"
                                    )
                                )
                            )
                        )
                    ),
                    FileNode(
                        "{XXXX-XXXX}", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                        description = "外接/可移除存储卷（SD 卡/U 盘，卷 ID）"
                    )
                )
            ),
            FileNode(
                "sdcard", nodeType = NodeType.FOLDER, folderType = FolderType.FOLDER,
                description = "符号链接到 /storage/emulated/0（用户主目录）"
            ),

            // App/data
            FileNode(
                "data", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "应用与系统数据分区",
                children = listOf(
                    FileNode(
                        "app", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "已安装应用的 APK（受权限保护）"
                    ),
                    FileNode(
                        "user", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "按用户分隔的应用数据根（如 /data/user/0）"
                    ),
                    FileNode(
                        "user_de", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "Direct Boot 域（设备未解锁也可用的少量数据）"
                    ),
                    FileNode(
                        "media", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "媒体数据（与 emulated 存储映射相关）"
                    ),
                    FileNode(
                        "system", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                        description = "系统级配置与状态数据"
                    )
                )
            ),

            // System partitions
            FileNode(
                "system", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "系统分区（framework、system apps、libs、bin）"
            ),
            FileNode(
                "system_ext", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "系统扩展分区（AOSP+设备扩展）"
            ),
            FileNode(
                "product", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "产品层系统组件（设备/市场差异）"
            ),
            FileNode(
                "vendor", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "厂商分区（驱动、HAL、厂商组件）"
            ),
            FileNode(
                "odm", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "ODM 分区（硬件适配，厂商定制）"
            ),

            // APEX and config
            FileNode(
                "apex", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "APEX 包挂载点（模块化系统组件）"
            ),
            FileNode(
                "linkerconfig", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "链接器配置（运行时库搜索路径）"
            ),
            FileNode(
                "etc", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "系统配置（SELinux、hosts 等）"
            ),

            // Virtual/kernel
            FileNode(
                "proc", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "内核与进程信息（虚拟文件系统，读时生成）"
            ),
            FileNode(
                "sys", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "设备/驱动状态（sysfs）"
            ),
            FileNode(
                "dev", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "设备节点（硬件接口）"
            ),

            // Mount/cache/metadata
            FileNode(
                "mnt", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "挂载点（包含 runtime 模式封装）"
            ),
            FileNode(
                "cache", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "系统缓存目录"
            ),
            FileNode(
                "metadata", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "设备元数据分区（加密/Provisioning 等）"
            ),

            // Optional/vendor-specific
            FileNode(
                "persist", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "持久化校准数据（传感器/基带，设备依赖）"
            ),
            FileNode(
                "oem", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "OEM 分区（部分设备保留）"
            ),
            FileNode(
                "config", nodeType = NodeType.FOLDER, folderType = FolderType.OTHER,
                description = "系统/内核配置（设备差异）"
            )
        )
    )
}

//fun getSampleCategories(): List<FileCategory> = listOf(
//    FileCategory("Images", Icons.Default.Image, count = 245, totalSizeMb = 512),
//    FileCategory("Videos", Icons.Default.Videocam, 87, 2048),
//    FileCategory("Audio", Icons.Default.MusicNote, 64, 980),
//    FileCategory("Documents", Icons.Default.Description, 120, 314),
//    FileCategory("APKs", Icons.Default.Android, 32, 822),
//    FileCategory("Archives", Icons.Default.Archive, 18, 420),
//    FileCategory("Others", Icons.Default.Folder, 53, 141),
//
//    FileCategory("Large Files", Icons.Default.Folder, 18, 7290),
//    FileCategory("Recent", Icons.Default.Schedule, 53, 141)
//)