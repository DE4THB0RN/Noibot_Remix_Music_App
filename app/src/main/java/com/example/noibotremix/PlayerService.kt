package com.example.noibotremix

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaNotification.ActionFactory
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.MediaStyleNotificationHelper
import androidx.media3.ui.PlayerNotificationManager
import com.google.common.collect.ImmutableList

class PlayerService : MediaSessionService() {
    //Definição dos PendingIntents que serão utilizados para notificações
    private var pendingPlayIntent: PendingIntent? = null
    private var pendingPauseIntent: PendingIntent? = null
    private var pendingNextIntent: PendingIntent? = null
    private var pendingPreviousIntent: PendingIntent? = null
    private var pendingDeleteIntent: PendingIntent? = null

    //Definição da MediaSession,Player e NotificationCompat.Builder
    private var mediaSession: MediaSession? = null
    private var exoplay: ExoPlayer? = null

    private var notificationManager: NotificationManager? = null

    //Definição das constantes
    private val channelId: String = R.string.app_name.toString() + " Music Channel"
    private val notifId: Int = 1111112

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        //O player
        //NÃO DELETA NÃO IMPORTA O QUE ACONTEÇA
        exoplay = ExoPlayer.Builder(this)
                .setHandleAudioBecomingNoisy(true)
                .build()

        //Pra que servem audios?
        //Atributos de audio
        //Animal
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build()
        exoplay!!.setAudioAttributes(audioAttributes, true)

        //Aqui é criada a MediaSession
        mediaSession = MediaSession.Builder(this, exoplay!!).build()

        //Coisas pra montar a notificação
        criarNotif()

    }

    //MÉTODO MUITO IMPORTANTE
    //Mantém o app no foreground
    @OptIn(UnstableApi::class)
    fun criarNotif() {
        //-------------------------------------------------------------------------------------------
        //Poço dos Intents (Alerta de dor de cabeça)
        //-------------------------------------------------------------------------------------------

        val playIntent = Intent(this, PlayerService::class.java)
        playIntent.setAction(PlayerNotificationManager.ACTION_PLAY)
        pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)

        val pauseIntent = Intent(this, PlayerService::class.java)
        pauseIntent.setAction(PlayerNotificationManager.ACTION_PAUSE)
        pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(this, PlayerService::class.java)
        nextIntent.setAction(PlayerNotificationManager.ACTION_NEXT)
        pendingNextIntent = PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE)

        val previousIntent = Intent(this, PlayerService::class.java)
        previousIntent.setAction(PlayerNotificationManager.ACTION_PREVIOUS)
        pendingPreviousIntent = PendingIntent.getService(this, 0, previousIntent, PendingIntent.FLAG_IMMUTABLE)

        val deleteIntent = Intent(this, PlayerService::class.java)
        deleteIntent.setAction(PlayerNotificationManager.ACTION_STOP)
        pendingDeleteIntent = PendingIntent.getService(this, 0, deleteIntent, PendingIntent.FLAG_IMMUTABLE)

        //-------------------------------------------------------------------------------------------------
        //Aqui acaba o poço
        //-------------------------------------------------------------------------------------------------

        //Aqui começa a brincadeira com notificação
        val notificationChannel = NotificationChannel(channelId,"Noibot Remix", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.createNotificationChannel(NotificationChannel(R.string.app_name.toString(), "Channel", NotificationManager.IMPORTANCE_HIGH))

        // Isso controla a notificação da MediaSession
        val asuka = NotificationCompat.Builder(this, R.string.app_name.toString())
        asuka.addAction(R.drawable.ic_notif_back, "Voltar", pendingPreviousIntent)
        if (exoplay!!.isPlaying) {
            asuka.addAction(R.drawable.ic_notif_pause, "Pause", pendingPauseIntent)
        } else if (!exoplay!!.isPlaying) {
            asuka.addAction(R.drawable.ic_notif_play, "Play", pendingPlayIntent)
        }
        asuka.addAction(R.drawable.ic_notif_next, "Pular", pendingNextIntent)
        asuka.setColor(Color.rgb(0, 0, 0))
                .setStyle(MediaStyleNotificationHelper.DecoratedMediaCustomViewStyle(mediaSession!!).setShowActionsInCompactView(0, 1, 2).setShowCancelButton(true).setCancelButtonIntent(pendingDeleteIntent!!))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setChannelId(channelId)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_noivern_notif)
                .setDeleteIntent(pendingDeleteIntent)
                .setColorized(true)


        notificationManager!!.notify(notifId, asuka.build())
        notificationManager!!.createNotificationChannel(notificationChannel)

    }

    //Definição das ações dos intents
    @OptIn(UnstableApi::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent!!.action

        if (PlayerNotificationManager.ACTION_PLAY == action) {
            if (!exoplay!!.isPlaying) exoplay!!.play()
        } else if (PlayerNotificationManager.ACTION_PAUSE == action) {
            if (exoplay!!.isPlaying) exoplay!!.pause()
        } else if (PlayerNotificationManager.ACTION_NEXT == action) {
            if (exoplay!!.hasNextMediaItem()) {
                exoplay!!.stop()
                exoplay!!.seekToNextMediaItem()
                exoplay!!.prepare()
                exoplay!!.play()
            }
        } else if (PlayerNotificationManager.ACTION_PREVIOUS == action) {
            if (exoplay!!.currentPosition < 5000) {
                if (exoplay!!.hasPreviousMediaItem()) {
                    exoplay!!.stop()
                    exoplay!!.seekToPreviousMediaItem()
                    exoplay!!.prepare()
                    exoplay!!.play()
                }
            } else {
                exoplay!!.seekTo(0)
            }
        } else if (PlayerNotificationManager.ACTION_STOP == action) {
            notificationManager!!.cancel(notifId)
            stopForeground(STOP_FOREGROUND_REMOVE)
            if (exoplay!!.isPlaying) exoplay!!.stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    //Quando o usuário remove o app dos apps recentes isso daqui acontece pra liberar o player
    override fun onTaskRemoved(rootIntent: Intent) {
        val playe = mediaSession?.player!!

        if(playe.playWhenReady){
            playe.pause()
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        stopSelf()
    }

    //Destruição do serviço causa isso daqui
    override fun onDestroy() {
        mediaSession?.run{
            player.release()
            release()
            mediaSession = null
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        Boomburst.is_playing = false
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
}


