package com.example.androidmaiden.core.experimental.job

/**
 * Interface to manage background tasks across platforms.
 */
interface BackgroundJobManager {
    /**
     * Schedules a notification to be shown after a certain delay.
     * @param id Unique identifier for the notification/job.
     * @param text The message to display.
     * @param delayMillis Delay in milliseconds before showing the notification.
     */
    fun scheduleNotification(id: String, text: String, delayMillis: Long)
    
    /**
     * Cancels a scheduled job.
     */
    fun cancelJob(id: String)
}
