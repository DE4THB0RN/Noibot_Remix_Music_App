package com.example.noibotremix;



import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {

    TextView titulo,tempoatual,tempototal;
    SeekBar barra;
    ImageView pauseplay,next,prev,noivern;
    ArrayList<AudioModel> songslist;
    List<MediaItem> mediaitems = new ArrayList<>();
    MediaItem media_atual;
    int valor;
    AudioModel musica_atual;

    ExoPlayer player;
    MediaSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        player = Boomburst.getExoplay(getApplicationContext());

        setContentView(R.layout.activity_player);
        titulo = findViewById(R.id.titulo_musica);
        tempoatual = findViewById(R.id.tempo_atual);
        tempototal = findViewById(R.id.tempo_total);
        barra = findViewById(R.id.barrinha);
        pauseplay = findViewById(R.id.pause_play);
        next = findViewById(R.id.frente);
        prev = findViewById(R.id.atras);
        noivern = findViewById(R.id.fotinha);


        titulo.setSelected(true);

        /*songslist = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LISTA");
        valor = (int) getIntent().getSerializableExtra("VALOR");

        for (AudioModel a : songslist)
        {
            mediaitems.add(MediaItem.fromUri(a.getPath()));
        }

        player.setMediaItems(mediaitems);



        Recursosmusicais();

        Player.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (player!=null){
                    barra.setProgress((int) ((player.getCurrentPosition())/player.getDuration()));
                    tempoatual.setText(converter(player.getCurrentPosition() + ""));
                    if (player.isPlaying()){
                        pauseplay.setImageResource(R.drawable.ic_baseline_pause_circle_24);

                    }
                    else{
                        pauseplay.setImageResource(R.drawable.ic_baseline_play_circle_24);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });
        barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(player!= null && b){
                    player.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/
    }

    /*@Override
    public void onStart() {
        super.onStart();
        SessionToken sessionToken =
                new SessionToken(this, new ComponentName(this, PlayerService.class));
        ListenableFuture<MediaController> controllerFuture =
                new MediaController.Builder(this, sessionToken).buildAsync();
        controllerFuture.addListener(() -> {
            // Call controllerFuture.get() to retrieve the MediaController.
            // MediaController implements the Player interface, so it can be
            // attached to the PlayerView UI component.
            playerView.setPlayer(controllerFuture.get());
        }, MoreExecutors.directExecutor());

    }*/


    /*void Recursosmusicais(){
         musica_atual = songslist.get(Boomburst.atual);
         media_atual = mediaitems.get(Boomburst.atual);
         titulo.setText(musica_atual.getNome());
         tempototal.setText(converter(musica_atual.getDuração()));
         pauseplay.setOnClickListener(v-> pause_play());
         next.setOnClickListener(v-> pular());
         prev.setOnClickListener(v-> voltar());

         if(valor == 1)
         {
             barra.setProgress((int) ((player.getCurrentPosition())/player.getDuration()));
             barra.setMax((int) player.getDuration());
             valor--;
         }
         else
         {
             player.seekTo(Boomburst.atual,0);
             toca_musica();
         }
    }
    private void toca_musica(){

        player.prepare();
        player.play();
        barra.setProgress(0);
        barra.setMax((int) player.getDuration());

    }

    private void pause_play(){
        if (player.isPlaying()){
            player.pause();
        }
        else{
            player.play();
        }
    }
    private void pular(){
            if (Boomburst.atual == songslist.size()-1){
                Boomburst.atual = -1;
            }
            Boomburst.atual+=1;
            player.seekToNextMediaItem();
            Recursosmusicais();
    }
    private void voltar(){
        if (Boomburst.atual == 0){
            return;
        }
        Boomburst.atual-=1;
        player.seekToPreviousMediaItem();
        Recursosmusicais();
    }
    public static String converter (String duracao){
        Long milis = Long.parseLong(duracao);
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milis / (1000 * 60 * 60));
        int minutes = (int) (milis % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milis % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }*/
}