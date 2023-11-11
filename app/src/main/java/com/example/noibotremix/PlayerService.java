package com.example.noibotremix;

import androidx.annotation.Nullable;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaSessionService;

public class PlayerService extends MediaSessionService {
    private MediaSession mediaSession;



    @Override
    public void onCreate()
    {
        super.onCreate();
        ExoPlayer exoplay = new ExoPlayer.Builder(this).build();
        mediaSession = new MediaSession.Builder(this, exoplay).build();



        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build();
        exoplay.setAudioAttributes(audioAttributes,true);

        final String channelId = R.string.app_name + " Music Channel";
        final int notifId = 1111111;
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
