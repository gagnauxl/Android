package com.example.notificationchannel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationHelper(base: Context) : ContextWrapper(base) {
    val channel1ID = "channel1ID"
    val channel1Name = "Channel 1"
    val channel2ID = "channel2ID"
    val channel2Name = "Channel 2"

    private var mManager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannels() {
        val channel1 =
            NotificationChannel(channel1ID, channel1Name, NotificationManager.IMPORTANCE_DEFAULT)
        with(channel1) {
            enableLights(true)
            enableVibration(true)
            lightColor = R.color.colorPrimary
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
        getManager()?.createNotificationChannel(channel1)

        val channel2 =
            NotificationChannel(channel2ID, channel2Name, NotificationManager.IMPORTANCE_DEFAULT)
        with(channel2) {
            enableLights(true)
            enableVibration(true)
            lightColor = R.color.colorPrimary
            lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
        getManager()?.createNotificationChannel(channel2)
    }

    fun getManager(): NotificationManager? {
        if (mManager == null) {
            mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        }
        return mManager
    }

    fun getChannel1Notification(title: String, message: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channel1ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_one)
    }
    fun getChannel2Notification(title: String, message: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channel2ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_two)
    }
}