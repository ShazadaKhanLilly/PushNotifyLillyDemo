package com.example.pushnotifylillydemo.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pushnotifylillydemo.MainActivity
import com.example.pushnotifylillydemo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushNotificationLillyService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.d("PushNotificationLilly", "Service created")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel"
            val name = "Default Channel"
            val descriptionText = "Channel for general notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("PushNotificationLilly", "Message received: ${message.data}")

        // Extract data from the notification payload
        val data = message.data
        val title = message.notification?.title?: "Default Title"
        val body = message.notification?.body ?: "Default Body Text"
        val url = data["url"] ?: return

        // Send the notification with the provided URL
        sendNotification(title, body, url)
    }

    private fun sendNotification(title: String?, body: String?, url: String?) {
        Log.d("PushNotificationLilly", "Sending notification: $title, $body, $url")
        // Create the intent for navigation
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to_support", true)
            putExtra("url_to_load", url)
        }

        // Create the pending intent
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build and send the notification
        val builder = NotificationCompat.Builder(this, "default_channel")
            .setSmallIcon(R.drawable.alert_red)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body)) // Expandable notification
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}