package com.example.noibotremix;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Player extends AppCompatActivity {

    TextView titulo,tempoatual,tempototal;
    SeekBar barra;
    ImageView pauseplay,next,prev,noivern;
    ArrayList<AudioModel> songslist;
    AudioModel musica_atual;
    MediaPlayer screech = Boomburst.getround();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        songslist = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LISTA");
        Recursosmusicais();

        Player.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (screech!=null){
                    barra.setProgress(screech.getCurrentPosition());
                    tempoatual.setText(converter(screech.getCurrentPosition()+""));
                    if (screech.isPlaying()){
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
                if(screech!= null && b){
                    screech.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    void Recursosmusicais(){
         musica_atual = songslist.get(Boomburst.atual);
         titulo.setText(musica_atual.getNome());
         tempototal.setText(converter(musica_atual.getDuração()));
         pauseplay.setOnClickListener(v-> pause_play());
         next.setOnClickListener(v-> pular());
         prev.setOnClickListener(v-> voltar());

         toca_musica();
         continua_musica();

    }
    private void toca_musica(){
            screech.reset();
            try
            {
                screech.setDataSource(musica_atual.getPath());
                screech.prepare();
                screech.start();
                barra.setProgress(0);
                barra.setMax(screech.getDuration());

            }
            catch (IOException e){
                e.printStackTrace();
            }

    }
    private void continua_musica()
    {
        screech.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                    Boomburst.atual+=1;
                    screech.reset();
                    Recursosmusicais();
            }
        });
    }

    private void pause_play(){
            if (screech.isPlaying()){
                screech.pause();
            }
            else{
                screech.start();
            }
    }
    private void pular(){
            if (Boomburst.atual == songslist.size()-1){
                return;
            }
            Boomburst.atual+=1;
            screech.reset();
            Recursosmusicais();
    }
    private void voltar(){
        if (Boomburst.atual == 0){
            return;
        }
        Boomburst.atual-=1;
        screech.reset();
        Recursosmusicais();
    }
    public static String converter (String duração){
        Long milis = Long.parseLong(duração);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toMinutes(milis) % TimeUnit.MINUTES.toSeconds(1));
    }
}