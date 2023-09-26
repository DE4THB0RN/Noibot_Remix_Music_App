package com.example.noibotremix;

import android.media.MediaPlayer;

public class Boomburst {
    static MediaPlayer round;

    public static MediaPlayer getround(){
        if (round == null){
            round = new MediaPlayer();
        }
        return round;
    }
    public static  int atual = -1;
}
