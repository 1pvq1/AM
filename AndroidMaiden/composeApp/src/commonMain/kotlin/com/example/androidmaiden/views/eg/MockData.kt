package com.example.androidmaiden.views.eg

import com.example.androidmaiden.data.FileMetadata

object FileListPagePreviewSamples {
    val now = 1715856000000L
    val lastMonth = now - 30L * 24 * 60 * 60 * 1000
    val twoMonthsAgo = now - 60L * 24 * 60 * 60 * 1000

    val images = listOf(
        FileMetadata(path = "mock/beach.jpg", name = "Beach_Sunset.jpg", isDirectory = false, lastModified = now, size = 3500000, parentPath = "mock", width = 4032, height = 3024, metadataStatus = 1),
        FileMetadata(path = "mock/mountain.png", name = "Mountain_View.png", isDirectory = false, lastModified = now - 100000, size = 5200000, parentPath = "mock", width = 1920, height = 1080, metadataStatus = 1),
        FileMetadata(path = "mock/family.webp", name = "Family_Dinner.webp", isDirectory = false, lastModified = lastMonth, size = 1200000, parentPath = "mock", width = 1200, height = 800, metadataStatus = 1),
        FileMetadata(path = "mock/old.jpg", name = "Old_Memory.jpg", isDirectory = false, lastModified = twoMonthsAgo, size = 800000, parentPath = "mock", width = 800, height = 600, metadataStatus = 1)
    )

    val videos = listOf(
        FileMetadata(path = "mock/summer.mp4", name = "Summer_Vacation.mp4", isDirectory = false, lastModified = now, size = 150000000, parentPath = "mock", duration = 125000, width = 1920, height = 1080, metadataStatus = 1),
        FileMetadata(path = "mock/birthday.mkv", name = "Birthday_Party.mkv", isDirectory = false, lastModified = lastMonth, size = 300000000, parentPath = "mock", duration = 3600000, width = 3840, height = 2160, metadataStatus = 1),
        FileMetadata(path = "mock/tutorial.mov", name = "Tutorial.mov", isDirectory = false, lastModified = lastMonth - 500000, size = 50000000, parentPath = "mock", duration = 45000, width = 1280, height = 720, metadataStatus = 1)
    )

    val audio = listOf(
        FileMetadata(path = "/storage/emulated/0/Music/Song_One.mp3", name = "Song_One.mp3", isDirectory = false, lastModified = now, size = 5000000, parentPath = "/storage/emulated/0/Music", artist = "Imagine Dragons", album = "Mercury - Act 1", duration = 210000, bitrate = 320000, metadataStatus = 1),
        FileMetadata(path = "/storage/emulated/0/Podcasts/Podcast_Ep1.wav", name = "Podcast_Ep1.wav", isDirectory = false, lastModified = lastMonth, size = 45000000, parentPath = "/storage/emulated/0/Podcasts", artist = "Lex Fridman", album = "Podcasts", duration = 7200000, bitrate = 128000, metadataStatus = 1),
        FileMetadata(path = "/storage/emulated/0/Recordings/Call_Recording.amr", name = "Call_Recording.amr", isDirectory = false, lastModified = lastMonth, size = 500000, parentPath = "/storage/emulated/0/Recordings", duration = 30000, metadataStatus = 1),
        FileMetadata(path = "/storage/emulated/0/WhatsApp/Media/Voice_Note.m4a", name = "Voice_Note.m4a", isDirectory = false, lastModified = twoMonthsAgo, size = 1000000, parentPath = "/storage/emulated/0/WhatsApp/Media", duration = 15000, metadataStatus = 1)
    )

    val documents = listOf(
        FileMetadata(path = "mock/report.pdf", name = "Project_Report.pdf", isDirectory = false, lastModified = now, size = 2500000, parentPath = "mock"),
        FileMetadata(path = "mock/budget.xlsx", name = "Budget_Plan.xlsx", isDirectory = false, lastModified = lastMonth, size = 1200000, parentPath = "mock"),
        FileMetadata(path = "mock/notes.txt", name = "Notes.txt", isDirectory = false, lastModified = twoMonthsAgo, size = 50000, parentPath = "mock"),
        FileMetadata(path = "mock/resume.docx", name = "Resume.docx", isDirectory = false, lastModified = now - 3600000, size = 800000, parentPath = "mock")
    )

    val apks = listOf(
        FileMetadata(path = "mock/game.apk", name = "Game_Launcher.apk", isDirectory = false, lastModified = now, size = 60000000, parentPath = "mock"),
        FileMetadata(path = "mock/messenger.apk", name = "Messenger_Lite.apk", isDirectory = false, lastModified = lastMonth, size = 15000000, parentPath = "mock")
    )

    val archives = listOf(
        FileMetadata(path = "mock/photos.zip", name = "Photos_2023.zip", isDirectory = false, lastModified = twoMonthsAgo, size = 500000000, parentPath = "mock"),
        FileMetadata(path = "mock/projects.rar", name = "Old_Projects.rar", isDirectory = false, lastModified = lastMonth, size = 1200000000, parentPath = "mock"),
        FileMetadata(path = "mock/backup.7z", name = "Backup.7z", isDirectory = false, lastModified = now, size = 250000000, parentPath = "mock")
    )
}
