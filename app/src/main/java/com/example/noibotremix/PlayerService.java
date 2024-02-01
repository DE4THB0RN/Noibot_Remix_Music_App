package com.example.noibotremix;

import static android.graphics.Color.rgb;

import static androidx.media3.common.util.NotificationUtil.IMPORTANCE_HIGH;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.CommandButton;
import androidx.media3.session.MediaNotification;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.ui.PlayerNotificationManager;

import com.google.common.collect.ImmutableList;

public class PlayerService extends MediaSessionService {

    MediaNotification.Provider provider;
    private MediaSession mediaSession;
    private ExoPlayer exoplay;
    private PlayerNotificationManager playerNotif;

    @Override
    public void onCreate() {
        super.onCreate();

        final String channelId = R.string.app_name + " Music Channel";
        final int notifId = 1111112;

        //O player
        //NÃO DELETA NÃO IMPORTA O QUE ACONTEÇA
        exoplay = new ExoPlayer.Builder(this)
                .setHandleAudioBecomingNoisy(true)
                .build();

        //Pra que servem audios?
        //Atributos de audio
        //Animal
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build();
        exoplay.setAudioAttributes(audioAttributes, true);


        //Coisas pra montar a notificação

        playerNotif = new PlayerNotificationManager.Builder(this, notifId, channelId)
                .setChannelImportance(IMPORTANCE_HIGH)
                .setNotificationListener(notifListener)
                .setSmallIconResourceId(R.drawable.ic_noivern_notif)
                .setChannelDescriptionResourceId(R.string.app_name)
                .setChannelNameResourceId(R.string.app_name)
                .build();

        playerNotif.setPlayer(exoplay);
        playerNotif.setPriority(NotificationCompat.PRIORITY_MAX);
        playerNotif.setUseNextActionInCompactView(true);
        playerNotif.setUsePreviousActionInCompactView(true);
        playerNotif.setUsePlayPauseActions(true);
        playerNotif.setUseRewindAction(false);
        playerNotif.setUseFastForwardAction(false);
        playerNotif.setUseStopAction(false);
        playerNotif.setUseChronometer(false);
        playerNotif.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        playerNotif.setColor(rgb(0, 0, 0));
        playerNotif.setColorized(true);

        //Aqui é criada a MediaSession
        mediaSession = new MediaSession.Builder(this, exoplay).build();



    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopForeground(true);
        playerNotif.setPlayer(null);
        playerNotif = null;
        if (exoplay.isPlaying()) exoplay.stop();
        exoplay.release();
        exoplay = null;
        mediaSession.release();
        mediaSession = null;
        super.onDestroy();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy()
    {
        stopForeground(true);
        playerNotif.setPlayer(null);
        playerNotif = null;
        if (exoplay.isPlaying()) exoplay.stop();
        exoplay.release();
        exoplay = null;
        mediaSession.release();
        mediaSession = null;
        super.onDestroy();
    }

    @Override
    public void onUpdateNotification(MediaSession session, boolean startInForegroundRequired) {
        stopForeground(true);
    }

    @Override
    public MediaSession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }

    private final PlayerNotificationManager.NotificationListener notifListener = new PlayerNotificationManager.NotificationListener() {
        @Override
        public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
            PlayerNotificationManager.NotificationListener.super.onNotificationCancelled(notificationId, dismissedByUser);
            stopForeground(true);
            if (exoplay.isPlaying()) {
                exoplay.stop();
                exoplay.release();
            }
        }

        @Override
        public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
            PlayerNotificationManager.NotificationListener.super.onNotificationPosted(notificationId, notification, ongoing);
            startForeground(notificationId,notification);
        }
    };

}


