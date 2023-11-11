package com.example.noibotremix;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioDeviceInfo;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import androidx.annotation.Nullable;
import androidx.media3.*;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.*;
import androidx.media3.ui.*;


import java.util.List;


public class Boomburst{

    static ExoPlayer exoplay;



    public static boolean player_criado = false;
    public static  int atual = -1;
}
