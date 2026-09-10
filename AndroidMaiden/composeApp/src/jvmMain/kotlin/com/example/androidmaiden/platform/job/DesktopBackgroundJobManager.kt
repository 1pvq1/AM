package com.example.androidmaiden.platform.job

import com.example.androidmaiden.core.experimental.job.BackgroundJobManager

/**
 * Desktop implementation of [BackgroundJobManager].
 * Placeholder for now as JVM background task management differs from Android.
 */
class DesktopBackgroundJobManager : BackgroundJobManager {
    override fun scheduleNotification(id: String, text: String, delayMillis: Long) {
        // TODO: Implement JVM-specific notification or scheduled task
        println("Scheduled JVM notification [$id]: $text after $delayMillis ms")
    }

    override fun cancelJob(id: String) {
        println("Cancelled JVM job: $id")
    }
}
