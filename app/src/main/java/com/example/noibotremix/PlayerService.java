package com.example.noibotremix;

import static android.graphics.Color.rgb;


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
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;



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

    //Definição dos PendingIntents que serão utilizados para notificações
    private PendingIntent pendingPlayIntent;
    private PendingIntent pendingPauseIntent;
    private PendingIntent pendingNextIntent;
    private PendingIntent pendingPreviousIntent;
    private PendingIntent pendingDeleteIntent;

    //Definição da MediaSession,Player e NotificationCompat.Builder
    private MediaSession mediaSession;
    private ExoPlayer exoplay;
    private NotificationCompat.Builder asuka;
    NotificationManager notificationManager = null;

    //Definição das constantes
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

        //Aqui começa a brincadeira com notificação

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(String.valueOf(R.string.app_name), "Channel", NotificationManager.IMPORTANCE_HIGH));
        }

        // Isso controla a notificação da MediaSession
        asuka = new NotificationCompat.Builder(this, String.valueOf(R.string.app_name));
        asuka.addAction(R.drawable.ic_notif_back, "Voltar", pendingPreviousIntent);
        if (exoplay.isPlaying()) {
            asuka.addAction(R.drawable.ic_notif_pause, "Pause", pendingPauseIntent);
        } else if (!exoplay.isPlaying()){
            asuka.addAction(R.drawable.ic_notif_play, "Play", pendingPlayIntent);
        }
        asuka.addAction(R.drawable.ic_notif_next, "Pular", pendingNextIntent);
        asuka.setColor(rgb(0, 0, 0))
                .setStyle(new MediaStyleNotificationHelper.DecoratedMediaCustomViewStyle(mediaSession).setShowActionsInCompactView(0,1,2).setShowCancelButton(true).setCancelButtonIntent(pendingDeleteIntent))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setChannelId(channelId)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_noivern_notif)
                .setDeleteIntent(pendingDeleteIntent)
                .setColorized(true);

        if (notificationManager != null) {
            notificationManager.notify(notifId, asuka.build());
        }

    }

    //Definição das ações dos intents
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
            notificationManager.cancel(notifId);
            stopForeground(true);
            if (exoplay.isPlaying()) exoplay.stop();
        }
        return super.onStartCommand(intent, flags, startId);
    }


    //Quando o usuário remove o app dos apps recentes isso daqui acontece pra liberar o player
    @Override
    public void onTaskRemoved(Intent rootIntent) {

        notificationManager.cancel(notifId);
        stopForeground(true);
        if (exoplay.isPlaying()) exoplay.stop();
        exoplay.release();
        exoplay = null;
        mediaSession.release();
        mediaSession = null;
        Boomburst.is_playing = false;
        Boomburst.atual = -1;
        super.onTaskRemoved(rootIntent);
    }

    //Destruição do serviço causa isso daqui
    @Override
    public void onDestroy()
    {
        stopForeground(true);
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
}


