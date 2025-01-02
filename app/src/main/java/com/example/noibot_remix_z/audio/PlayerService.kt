package com.example.noibot_remix_z.audio

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.noibot_remix_z.NotificationUtils

class PlayerService : MediaSessionService() {

    //Definição da MediaSession,Player e NotificationCompat.Builder
    var mediaSession: MediaSession? = null
    private var exoplay: ExoPlayer? = null


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

        //Coisas pra postar a notificação
        NotificationUtils.createNotificationChannel(this)
        NotificationUtils.startForeground(this)

    }

    //Quando o usuário remove o app dos apps recentes isso daqui acontece pra liberar o player
//    override fun onTaskRemoved(rootIntent: Intent) {
//        val playe = mediaSession?.player!!
//
//        if(playe.playWhenReady || playe.isPlaying){
//            playe.pause()
//            NotificationUtils.killNotification()
//            stopForeground(STOP_FOREGROUND_REMOVE)
//        }
//        stopSelf()
//    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val playe = mediaSession?.player!!

        if(playe.playWhenReady || playe.isPlaying){
            playe.pause()
            NotificationUtils.killNotification()
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    //Destruição do serviço causa isso daqui
    override fun onDestroy() {
        mediaSession?.run{
            player.release()
            release()
            mediaSession = null
        }
        NotificationUtils.killNotification()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        Boomburst.is_playing = false
        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
}