package com.example.pushnotificationshandlingapp.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pushnotificationshandlingapp.MainActivity
import com.example.pushnotificationshandlingapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FireBasePush"
    private var i = 0
    private var title = ""
    private var type = ""
    private var bookingId =""
    private var message: String? = ""
    private var CHANNEL_ID = "specialoclockuser"
    private lateinit var soundUri: Uri

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.e(TAG, "Refreshed token: $refreshedToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FireBasePush", "Notification: ${remoteMessage}")
        Log.d("FireBasePush", "noti=  ${remoteMessage.notification}")
        Log.d("FireBasePush", "Notification data : ${remoteMessage.data}")
        Log.d("FireBasePush", "Notification data TITLE : ${remoteMessage.data["title"]}")
        Log.d("FireBasePush", "Notification data msg  : ${remoteMessage.data["message"]}")
        Log.d("FireBasePush", "Notification bbokijnn  : ${remoteMessage.data["booking_primary_id"].toString()}")

//        title = remoteMessage.notification?.title.toString()
//        message = remoteMessage.notification?.body.toString()

        try {
            if (remoteMessage.data["title"]?.isNotEmpty() == true){
                title = remoteMessage.data["title"].toString()
                message = remoteMessage.data["message"]
                type = remoteMessage.data["type"].toString()
                bookingId = remoteMessage.data["booking_primary_id"].toString()
            }
//            else{
//                title = remoteMessage.notification?.title.toString()
//                message = remoteMessage.notification?.body.toString()
//                type = remoteMessage.data["type"].toString()
//                bookingId = remoteMessage.data["booking_primary_id"].toString()
//            }

            when(type){
                //type1 - booking completion notification
                "1" -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("type", type)
                    intent.putExtra("booking_id", bookingId)
                    makePush(intent)
                }
                //chat message case
                // if (Constants.isOnChat == true)
                //donot launch notification
                //else
                // show notification
            }
        } catch (e: Exception) {
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun makePush(intent: Intent?) {
        //foreground handling
        val pendingIntent = PendingIntent.getActivity(
            this, i,
            intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        intent?.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val channelId = CHANNEL_ID
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        Log.d(TAG, "makePush: "+title)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(notificationIcon)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "specialoclockuser",
                NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.MAGENTA
            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    private val notificationIcon: Int
        @SuppressLint("ObsoleteSdkInt")
        get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.drawable.ic_launcher_background else R.mipmap.ic_launcher
        }
}