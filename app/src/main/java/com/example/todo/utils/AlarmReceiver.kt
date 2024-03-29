package com.example.todo.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.todo.R
import com.example.todo.ui.AlarmActivity

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_DESCRIPTION = "description"
        const val EXTRA_TIME = "time"
        private var ID_ALARM = 100
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val title = "Task Reminder"
        val message = intent!!.getStringExtra(EXTRA_MESSAGE)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val time = intent.getStringExtra(EXTRA_TIME)

        showAlarmNotification(context!!, title, message!!, description!!, time!!, ID_ALARM)
    }

    private fun showAlarmNotification(context: Context, title: String, message: String, description: String, time: String, notifId: Int) {
        val channelId = "Channel_1"
        val channelName = "Todo Alarm Channel"

        val intent = Intent(context, AlarmActivity::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_DESCRIPTION, description)
        intent.putExtra(EXTRA_TIME, time)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_list)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }
}