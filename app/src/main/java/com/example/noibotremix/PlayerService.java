package com.example.noibotremix;

import static android.graphics.Color.rgb;

import static androidx.media3.common.util.NotificationUtil.IMPORTANCE_HIGH;
import static androidx.media3.ui.PlayerNotificationManager.ACTION_NEXT;
import static androidx.media3.ui.PlayerNotificationManager.ACTION_PAUSE;
import static androidx.media3.ui.PlayerNotificationManager.ACTION_PLAY;
import static androidx.media3.ui.PlayerNotificationManager.ACTION_PREVIOUS;
import static androidx.media3.ui.PlayerNotificationManager.ACTION_STOP;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.CommandButton;
import androidx.media3.session.MediaNotification;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;
import androidx.media3.session.MediaStyleNotificationHelper;
import androidx.media3.ui.PlayerNotificationManager;

import com.google.common.collect.ImmutableList;

public class PlayerService extends MediaSessionService {


    private PendingIntent pendingPlayIntent;
    private PendingIntent pendingPauseIntent;
    private PendingIntent pendingNextIntent;
    private PendingIntent pendingPreviousIntent;
    private PendingIntent pendingDeleteIntent;
    private MediaSession mediaSession;
    private ExoPlayer exoplay;

    //private PlayerNotificationManager playerNotif;

    private NotificationCompat.Builder asuka;
    final String channelId = R.string.app_name + " Music Channel";
    final int notifId = 1111112;
    @Override
    public void onCreate() {
        super.onCreate();

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

        //Aqui é criada a MediaSession
        mediaSession = new MediaSession.Builder(this, exoplay).build();

        //Coisas pra montar a notificação

        this.setMediaNotificationProvider(new MediaNotification.Provider() {
            @Override
            public MediaNotification createNotification(MediaSession session, ImmutableList<CommandButton> customLayout, MediaNotification.ActionFactory actionFactory, Callback onNotificationChangedCallback) {
                criarNotif(mediaSession);
                return new MediaNotification(notifId, asuka.build());
            }

            @Override
            public boolean handleCustomCommand(MediaSession session, String action, Bundle extras) {
                return false;
            }

        });

       /* playerNotif = new PlayerNotificationManager.Builder(this, notifId, channelId)
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
        playerNotif.setColorized(true);*/


        exoplay.addListener(new Player.Listener() {
            @Override
            public void onEvents(Player player, Player.Events events) {
                if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) updateNotificationPlaybackState();
                Player.Listener.super.onEvents(player, events);
            }
        });

    }

    //MÉTODO MUITO IMPORTANTE
    //Mantém o app no foreground
    public void criarNotif(MediaSession session) {

        //-------------------------------------------------------------------------------------------
        //Poço dos Intents (Alerta de dor de cabeça)
        //-------------------------------------------------------------------------------------------
        Intent playIntent = new Intent(this, PlayerService.class);
        playIntent.setAction(ACTION_PLAY);
        pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent pauseIntent = new Intent(this, PlayerService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, PlayerService.class);
        nextIntent.setAction(ACTION_NEXT);
        pendingNextIntent = PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent previousIntent = new Intent(this, PlayerService.class);
        previousIntent.setAction(ACTION_PREVIOUS);
        pendingPreviousIntent = PendingIntent.getService(this, 0, previousIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent deleteIntent = new Intent(this, PlayerService.class);
        deleteIntent.setAction(ACTION_STOP);
        pendingDeleteIntent = PendingIntent.getService(this, 0, deleteIntent, PendingIntent.FLAG_IMMUTABLE);
        //-------------------------------------------------------------------------------------------------
        //Aqui acaba o poço
        //-------------------------------------------------------------------------------------------------


        NotificationManager notificationManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(String.valueOf(R.string.app_name), "Channel", NotificationManager.IMPORTANCE_HIGH));
        }

        // Isso controla a notificação da MediaSession
        asuka = new NotificationCompat.Builder(this, String.valueOf(R.string.app_name))
                .addAction(R.drawable.ic_action_previous,"Voltar",pendingPreviousIntent)
                .addAction(R.drawable.ic_action_pause,"Pause",pendingPauseIntent)
                .addAction(R.drawable.ic_action_next,"Pular",pendingNextIntent)
                .setColor(rgb(0, 0, 0))
                .setColorized(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_noivern_notif)
                .setDeleteIntent(pendingDeleteIntent)
                .setStyle(new MediaStyleNotificationHelper.DecoratedMediaCustomViewStyle(session)
                        .setShowActionsInCompactView(0,1,2));



        if (notificationManager != null) {
            notificationManager.notify(notifId, asuka.build());
        }

    }

    public void updateNotificationPlaybackState() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

        builder.addAction(R.drawable.ic_action_previous, "Voltar", pendingPreviousIntent);
        if (exoplay.isPlaying()) {
            builder.addAction(R.drawable.ic_action_pause, "Pause", pendingPauseIntent);
        } else {
            builder.addAction(R.drawable.ic_action_play, "Play", pendingPlayIntent);
        }
        builder.addAction(R.drawable.ic_action_next, "Pular", pendingNextIntent);
        builder.setColor(rgb(0, 0, 0))
                .setColorized(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_noivern_notif)
                .setDeleteIntent(pendingDeleteIntent)
                .setStyle(new MediaStyleNotificationHelper.DecoratedMediaCustomViewStyle(mediaSession)
                        .setShowActionsInCompactView(0,1,2));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notifId, builder.build());
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (ACTION_PLAY.equals(action)) {
            if (!exoplay.isPlaying()) exoplay.play();
        } else if (ACTION_PAUSE.equals(action)) {
            if(exoplay.isPlaying()) exoplay.pause();
        } else if (ACTION_NEXT.equals(action)) {
            if (exoplay.hasNextMediaItem())
            {
                exoplay.stop();
                exoplay.seekToNextMediaItem();
                exoplay.prepare();
                exoplay.play();
            }
        } else if (ACTION_PREVIOUS.equals(action)) {
            if(exoplay.getCurrentPosition() < 5000)
            {
                if (exoplay.hasPreviousMediaItem())
                {
                    exoplay.stop();
                    exoplay.seekToPreviousMediaItem();
                    exoplay.prepare();
                    exoplay.play();
                }
            }
            else
            {
                exoplay.seekTo(0);
            }
        }
        else if(ACTION_STOP.equals(action))
        {
            stopForeground(true);
            if (exoplay.isPlaying()) exoplay.stop();
            exoplay.release();
            exoplay = null;
            mediaSession.release();
            mediaSession = null;
            Boomburst.is_playing = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }




    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopForeground(true);
        //playerNotif.setPlayer(null);
        //playerNotif = null;
        if (exoplay.isPlaying()) exoplay.stop();
        exoplay.release();
        exoplay = null;
        mediaSession.release();
        mediaSession = null;
        Boomburst.is_playing = false;
        super.onTaskRemoved(rootIntent);
    }


    @Override
    public void onDestroy()
    {
        stopForeground(true);
        //playerNotif.setPlayer(null);
        //playerNotif = null;
        if (exoplay.isPlaying()) exoplay.stop();
        exoplay.release();
        exoplay = null;
        mediaSession.release();
        mediaSession = null;
        Boomburst.is_playing = false;
        super.onDestroy();
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
                Boomburst.is_playing = false;
            }
        }

        @Override
        public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
            PlayerNotificationManager.NotificationListener.super.onNotificationPosted(notificationId, notification, ongoing);
            startForeground(notificationId,notification);
        }
    };



}


