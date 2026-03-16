package com.example.androidmaiden.views.eg

import com.example.androidmaiden.model.FileSysNode
import com.example.androidmaiden.model.NodeType

object FileListPagePreviewSamples {
    val now = 1715856000000L
    val lastMonth = now - 30L * 24 * 60 * 60 * 1000
    val twoMonthsAgo = now - 60L * 24 * 60 * 60 * 1000

    val images = listOf(
        FileSysNode("Beach_Sunset.jpg", 3500000, now, NodeType.FILE, path = "mock/beach.jpg"),
        FileSysNode("Mountain_View.png", 5200000, now - 100000, NodeType.FILE, path = "mock/mountain.png"),
        FileSysNode("Family_Dinner.webp", 1200000, lastMonth, NodeType.FILE, path = "mock/family.webp"),
        FileSysNode("Old_Memory.jpg", 800000, twoMonthsAgo, NodeType.FILE, path = "mock/old.jpg")
    )

    val videos = listOf(
        FileSysNode("Summer_Vacation.mp4", 150000000, now, NodeType.FILE, path = "mock/summer.mp4"),
        FileSysNode("Birthday_Party.mkv", 300000000, lastMonth, NodeType.FILE, path = "mock/birthday.mkv"),
        FileSysNode("Tutorial.mov", 50000000, lastMonth - 500000, NodeType.FILE, path = "mock/tutorial.mov")
    )

    val audio = listOf(
        FileSysNode("Song_One.mp3", 5000000, now, NodeType.FILE, path = "/storage/emulated/0/Music/Song_One.mp3"),
        FileSysNode("Podcast_Ep1.wav", 45000000, lastMonth, NodeType.FILE, path = "/storage/emulated/0/Podcasts/Podcast_Ep1.wav"),
        FileSysNode("Call_Recording.amr", 500000, lastMonth, NodeType.FILE, path = "/storage/emulated/0/Recordings/Call_Recording.amr"),
        FileSysNode("Voice_Note.m4a", 1000000, twoMonthsAgo, NodeType.FILE, path = "/storage/emulated/0/WhatsApp/Media/WhatsApp Voice Notes/Voice_Note.m4a")
    )

    val documents = listOf(
        FileSysNode("Project_Report.pdf", 2500000, now, NodeType.FILE),
        FileSysNode("Budget_Plan.xlsx", 1200000, lastMonth, NodeType.FILE),
        FileSysNode("Notes.txt", 50000, twoMonthsAgo, NodeType.FILE),
        FileSysNode("Resume.docx", 800000, now - 3600000, NodeType.FILE)
    )

    val apks = listOf(
        FileSysNode("Game_Launcher.apk", 60000000, now, NodeType.FILE),
        FileSysNode("Messenger_Lite.apk", 15000000, lastMonth, NodeType.FILE)
    )

    val archives = listOf(
        FileSysNode("Photos_2023.zip", 500000000, twoMonthsAgo, NodeType.FILE),
        FileSysNode("Old_Projects.rar", 1200000000, lastMonth, NodeType.FILE),
        FileSysNode("Backup.7z", 250000000, now, NodeType.FILE)
    )
}