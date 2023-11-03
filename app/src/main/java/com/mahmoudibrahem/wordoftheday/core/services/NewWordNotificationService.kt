package com.mahmoudibrahem.wordoftheday.core.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mahmoudibrahem.wordoftheday.R
import com.mahmoudibrahem.wordoftheday.core.util.Constants.NOTIFICATION_CHANNEL_ID
import com.mahmoudibrahem.wordoftheday.core.util.Constants.NOTIFICATION_CHANNEL_NAME

class NewWordNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("```TAG```", "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.data["title"]
        val content = message.data["body"]
        if (title != null && content != null) {
            createNotification(title, content)
        }
    }

    private fun createNotification(
        title: String,
        content: String
    ) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            createNotificationChannel(notificationManager)
        }

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).apply {
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(R.mipmap.ic_launcher)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
        notificationManager.notify(0, builder.build())
    }

    private fun createNotificationChannel(nm: NotificationManager) {
        val appNotificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        nm.createNotificationChannel(appNotificationChannel)

    }
}