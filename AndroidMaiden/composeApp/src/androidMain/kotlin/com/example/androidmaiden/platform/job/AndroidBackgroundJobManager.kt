package com.example.androidmaiden.platform.job

import android.content.Context
import androidx.work.*
import com.example.androidmaiden.core.experimental.job.BackgroundJobManager
import com.example.androidmaiden.worker.TodoNotificationWorker
import java.util.concurrent.TimeUnit

/**
 * Android implementation of [BackgroundJobManager] using WorkManager.
 */
class AndroidBackgroundJobManager(private val context: Context) : BackgroundJobManager {
    
    override fun scheduleNotification(id: String, text: String, delayMillis: Long) {
        val workRequest = OneTimeWorkRequestBuilder<TodoNotificationWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(
                "todo_text" to text,
                "todo_id" to id.hashCode().toLong()
            ))
            .addTag(id)
            .build()
            
        WorkManager.getInstance(context).enqueueUniqueWork(
            id,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun cancelJob(id: String) {
        WorkManager.getInstance(context).cancelUniqueWork(id)
    }
}
