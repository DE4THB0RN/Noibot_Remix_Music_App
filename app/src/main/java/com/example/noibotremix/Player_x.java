package com.example.noibotremix;

import android.content.ComponentName;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;
import androidx.media3.ui.PlayerView;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Player_x extends AppCompatActivity
{
    PlayerView playerView;
    ArrayList<AudioModel> songslist;
    int valor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Aqui está setando o layout E o player(em tese)
        setContentView(R.layout.activity_player_x);
        playerView = findViewById(R.id.player_exo);
        songslist = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LISTA");
        valor = (int) getIntent().getSerializableExtra("VALOR");
    }

    @Override
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
            try {
                playerView.setPlayer(controllerFuture.get());
                playerView.getPlayer().addMediaItems(pegarMusicas());
                playerView.getPlayer().seekTo(Boomburst.atual,0);
                playerView.getPlayer().prepare();
                playerView.getPlayer().setPlayWhenReady(true);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, MoreExecutors.directExecutor());
    }


    //Pega todas as músicas da lista que foi jogada aqui
    //Não sei se vai funcionar 100% sabe
    //Maaaas torço pro ExoPlayer funcionar direitinho
    private List<MediaItem> pegarMusicas()
    {
        List<MediaItem> mediaitems = new ArrayList<>();

        for (AudioModel a : songslist)
        {
            mediaitems.add(MediaItem.fromUri(a.getPath()));
        }

        return mediaitems;
    }
}
