package com.example.noibotremix;

import static android.graphics.Color.rgb;

import android.content.ComponentName;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;
import androidx.media3.ui.PlayerControlView;
import androidx.media3.ui.PlayerNotificationManager;
import androidx.media3.ui.PlayerView;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Player_x extends AppCompatActivity
{

    PlayerView playerView;
    ListenableFuture<MediaController> controllerFuture;
    ArrayList<AudioModel> songslist;
    int valor;
    TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Aqui está setando o layout E o player(em tese)
        setContentView(R.layout.activity_player_x);
        playerView = findViewById(R.id.player_exo);
        playerView.setShowNextButton(true);
        playerView.setShowPreviousButton(true);
        playerView.setBackgroundColor(getResources().getColor(R.color.black));
        playerView.setShowRewindButton(false);
        playerView.setShowFastForwardButton(false);
        playerView.setArtworkDisplayMode(PlayerView.ARTWORK_DISPLAY_MODE_OFF);
        playerView.setShowShuffleButton(true);
        titulo = findViewById(R.id.titulo_musica);
        songslist = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LISTA");
        valor = (int) getIntent().getSerializableExtra("VALOR");

    }

    @Override
    public void onStart() {
        super.onStart();
        //Session token abre a sessão do player

            SessionToken sessionToken =
                    new SessionToken(this, new ComponentName(this, PlayerService.class));
            controllerFuture =
                    new MediaController.Builder(this, sessionToken).buildAsync();

            controllerFuture.addListener(() -> {
                // Controller serve literalmente pra isso
                //Ele controla a música
                try {

                    playerView.setPlayer(controllerFuture.get());
                    if(!Boomburst.is_playing)
                    {
                        playerView.getPlayer().addMediaItems(pegarMusicas());
                        Boomburst.is_playing = true;
                        musiquinha();
                    }
                    else if(Boomburst.is_playing && playerView.getPlayer().getCurrentMediaItemIndex() != Boomburst.atual){
                        musiquinha();
                    }
                    else{
                        telinha();
                    }

                    playerView.getPlayer().addListener(new Player.Listener() {
                    @Override
                    public void onEvents(Player player, Player.Events events) {
                            Player.Listener.super.onEvents(player, events);
                            if(events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)){
                                Boomburst.atual = player.getCurrentMediaItemIndex();
                                titulo.setText(Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.title);
                            }
                        }
                    });
                } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            }, MoreExecutors.directExecutor());

    }

    private void musiquinha()
    {
        playerView.getPlayer().seekTo(Boomburst.atual,0);
        playerView.getPlayer().prepare();
        playerView.getPlayer().setPlayWhenReady(true);
        telinha();
    }

    private void telinha(){

        titulo.setText(Objects.requireNonNull(playerView.getPlayer().getCurrentMediaItem()).mediaMetadata.title);

    }



    //Pega todas as músicas da lista que foi jogada aqui
    //Não sei se vai funcionar 100% sabe
    //Maaaas torço pro ExoPlayer funcionar direitinho
    private List<MediaItem> pegarMusicas()
    {
        List<MediaItem> mediaitems = new ArrayList<>();
        MediaItem novo;
        int id = 0;
        Drawable d = getDrawable(R.drawable.noivern_icon);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();
        for (AudioModel a : songslist)
        {
            novo = new MediaItem.Builder()
                    .setUri(a.getPath())
                    .setMediaId("id_"+id)
                    .setMediaMetadata(new MediaMetadata.Builder()
                            .setTitle(a.getNome())
                            .setArtworkData(bitmapdata,MediaMetadata.PICTURE_TYPE_ILLUSTRATION)
                            .build())
                    .build();
            mediaitems.add(novo);
            id++;
        }

        return mediaitems;
    }



}
