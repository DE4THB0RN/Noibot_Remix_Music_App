package com.example.noibot_remix_z

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import androidx.core.app.NotificationCompat

object NotificationUtils {
    private const val CHANNEL_ID : String = "Noibot Remix".toString() + " Music Channel"
    private const val CHANNEL_NAME : String = "Boomburst"
    private const val NOTIF_ID : Int = 1111112
    private var notificationManager : NotificationManager? = null

    fun createNotificationChannel(context : Context){
        //Aqui começa a brincadeira com notificação
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(notificationChannel)
    }

    fun startForeground(service : Service){
        //Hora de começar a festinha hahaha
        val asuka = NotificationCompat.Builder(service, CHANNEL_ID).build()
        service.startForeground(NOTIF_ID,asuka)
    }

    fun killNotification(){
        //A festinha acaba aqui...
        notificationManager!!.cancel(NOTIF_ID)
    }

}