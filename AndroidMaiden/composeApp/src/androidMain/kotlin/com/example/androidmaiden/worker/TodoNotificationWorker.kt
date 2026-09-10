package com.example.androidmaiden.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class TodoNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val todoText = inputData.getString("todo_text") ?: "Task reminder"
        val todoId = inputData.getLong("todo_id", -1L)

        showNotification(todoText, todoId.toInt())

        return Result.success()
    }

    private fun showNotification(text: String, id: Int) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "todo_notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Todo Reminders", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Todo Reminder")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // Using a system icon for now
            .setAutoCancel(true)
            .build()

        notificationManager.notify(id, notification)
    }
}
