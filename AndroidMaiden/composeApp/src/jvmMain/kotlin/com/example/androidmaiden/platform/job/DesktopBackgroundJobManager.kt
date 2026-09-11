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

    override fun schedulePeriodicNotification(id: String, text: String, intervalMillis: Long, initialDelayMillis: Long) {
        // TODO: Implement JVM-specific periodic task
        println("Scheduled periodic JVM notification [$id]: $text every $intervalMillis ms")
    }

    override fun cancelJob(id: String) {
        println("Cancelled JVM job: $id")
    }
}
