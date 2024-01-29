package com.example.noibotremix;

import android.content.Context;

import androidx.media3.exoplayer.ExoPlayer;

public class Boomburst{

    static ExoPlayer exoplay;

    public static boolean player_criado = false;
    public static  int atual = -1;

    public static ExoPlayer getExoplay(Context context)
    {
        exoplay = new ExoPlayer.Builder(context).build();
        return exoplay;
    }
}
