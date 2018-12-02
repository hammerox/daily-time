package com.mcustodio.dailytime

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mcustodio.dailytime.ui.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.*

class FirebaseTopics :  FirebaseMessagingService() {


    val messaging = FirebaseMessaging.getInstance()
    private val CHANNEL_ID = "com.mcustodio.dailytime.NOTIFICATION_CHANNEL"
    private val GROUP_ID = "com.mcustodio.dailytime.NOTIFICATION_GROUP"
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }


    override fun onMessageReceived(data: RemoteMessage?) {
        super.onMessageReceived(data)

        val notification = buildDefaultNotification(data)
        notification.addClickAction()
        NotificationManagerCompat.from(this).notify(createID(), notification.build())
    }


    private fun startNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,"DailyTime", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun buildDefaultNotification(data: RemoteMessage?): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_timer_black_24dp)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setAutoCancel(true)

        data?.notification?.title?.let { builder.setContentTitle(it) }
        data?.notification?.body?.let { builder.setContentText(it) }

        return builder
    }


    private fun NotificationCompat.Builder.addClickAction() {
        val intent = Intent(this@FirebaseTopics, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this@FirebaseTopics, 0, intent, 0)
        this.setContentIntent(pendingIntent)
    }


    fun subscribeTo(topic: String?) : Task<Void>?{
        return topic?.let {
            messaging.subscribeToTopic(topic)
                .addOnSuccessListener {  }
                .addOnFailureListener {  }
        }
    }

    fun unsubscribeFrom(topic: String?) : Task<Void>? {
        return topic?.let {
            messaging.unsubscribeFromTopic(topic)
                .addOnSuccessListener {  }
                .addOnFailureListener {  }
        }
    }

    private fun createID(): Int {
        val now = Date()
        return Integer.parseInt(SimpleDateFormat("ddHHmmss", Locale.US).format(now))
    }
}