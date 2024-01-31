package com.example.noibotremix;

import static android.graphics.Color.rgb;

import static androidx.media3.common.util.NotificationUtil.IMPORTANCE_HIGH;

import android.support.v4.media.session.PlaybackStateCompat;


import androidx.core.app.NotificationCompat;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.ui.PlayerNotificationManager;

public class PlayerService extends MediaSessionService {

    private MediaSession mediaSession;

    @Override
    public void onCreate()
    {
        super.onCreate();

        final String channelId = R.string.app_name + " Music Channel";
        final int notifId = 1111112;

        ExoPlayer exoplay = new ExoPlayer.Builder(this)
                .setHandleAudioBecomingNoisy(true)
                .build();



        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build();
        exoplay.setAudioAttributes(audioAttributes,true);


        //Coisas pra montar a notificação

        PlayerNotificationManager playerNotif = new PlayerNotificationManager.Builder(this,notifId,channelId)
                .setChannelImportance(IMPORTANCE_HIGH)
                .setNotificationListener(new PlayerNotificationManager.NotificationListener() {
                    @Override
                    public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
                        PlayerNotificationManager.NotificationListener.super.onNotificationCancelled(notificationId, dismissedByUser);
                    }
                })
                .setNextActionIconResourceId(R.drawable.ic_baseline_skip_next_24)
                .setPreviousActionIconResourceId(R.drawable.ic_baseline_skip_previous_24)
                .setPauseActionIconResourceId(R.drawable.ic_baseline_pause_circle_24)
                .setPlayActionIconResourceId(R.drawable.ic_baseline_play_circle_24)
                .setSmallIconResourceId(R.drawable.noivern_smallo)
                .build();

        playerNotif.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        playerNotif.setColor(rgb(0,0,0));
        playerNotif.setColorized(true);
        playerNotif.setPlayer(exoplay);


        //Aqui é criada a MediaSession
        mediaSession = new MediaSession.Builder(this, exoplay).build();
    }

    @Override
    public void onDestroy()
    {
        mediaSession.getPlayer().release();
        mediaSession.release();
        mediaSession = null;
        super.onDestroy();
    }



    @Override
    public MediaSession onGetSession(MediaSession.ControllerInfo controllerInfo) {
        return mediaSession;
    }



}


