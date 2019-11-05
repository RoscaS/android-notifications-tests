package com.example.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread


class NotifyActivity : AppCompatActivity() {

  companion object {
    private val GROUP_KEY_NOTIFY = "group_key_notify"
  }
  /*------------------------------------------------------------------*\
  |*							                ATTRIBUTES
  \*------------------------------------------------------------------*/

  private var notificationManager: NotificationManager? = null
  private var notificationID: Int = 100

  /*------------------------------------------------------------------*\
  |*							                HOOKS
  \*------------------------------------------------------------------*/

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    notificationManager =
      getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    createNotificationChannel(
      "com.example.notifications.news",
      "NotifyDemo News",
      "Example News Channel"
    )

    btn_notify.setOnClickListener {
      thread {
        val title = "New message"
        val body = { x: String -> "You have a new message from $x!"}
        sendNotification(notificationID++, title, body("José"))
        Thread.sleep(500)
        // sendNotification(notificationID++, title, body("Paulie"))
        // Thread.sleep(500)
        // sendNotification(notificationID++, title, body("Vlad"))
        // Thread.sleep(500)
      }
    }
  }

  private fun createNotificationChannel(id: String,
                                        name: String,
                                        description: String) {

    val importance = NotificationManager.IMPORTANCE_LOW
    val channel = NotificationChannel(id, name, importance)

    channel.description = description
    channel.enableLights(true)
    channel.lightColor = Color.RED
    channel.enableVibration(true)
    channel.vibrationPattern =
      longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
    notificationManager?.createNotificationChannel(channel)
  }

  private fun sendNotification(id: Int, title: String, body: String ) {
    val resultIntent = Intent(this, ResultActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
      this, 0, resultIntent,
      PendingIntent.FLAG_UPDATE_CURRENT
    )

    val icon = Icon.createWithResource(this, android.R.drawable.ic_dialog_info)
    val action: Notification.Action =
      Notification.Action.Builder(icon, "Open", pendingIntent).build()

    val channelID = "com.example.notifications.news"

    Log.d("NOTIF", "id: $notificationID")

    val notification = Notification.Builder(this, channelID)
      .setContentTitle(title)
      .setContentText(body)
      .setSmallIcon(android.R.drawable.ic_dialog_info)
      .setChannelId(channelID)
      .setContentIntent(pendingIntent)
      .setActions(action)
      .setAutoCancel(true)
      // .setGroup(GROUP_KEY_NOTIFY)
      // .setGroupSummary(if(notificationID == 103) true else false)
      .build()

    notificationManager?.notify(id, notification)
  }
}