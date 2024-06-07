package com.example.noibotremix

import android.content.ComponentName
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.io.ByteArrayOutputStream
import java.util.Objects
import java.util.concurrent.ExecutionException

class PlayerX : AppCompatActivity() {
    private var playerView: PlayerView? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var songslist: ArrayList<AudioModel>? = null
    private var valor: Int = 0
    var titulo: TextView? = null

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Aqui está setando o layout E o player(em tese)
        setContentView(R.layout.activity_player_x)
        playerView = findViewById(R.id.player_exo)
        playerView!!.setShowNextButton(true)
        playerView!!.setShowPreviousButton(true)
        playerView!!.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        playerView!!.setShowRewindButton(false)
        playerView!!.setShowFastForwardButton(false)
        playerView!!.setArtworkDisplayMode(PlayerView.ARTWORK_DISPLAY_MODE_OFF)
        playerView!!.setShowShuffleButton(true)
        titulo = findViewById(R.id.titulo_musica)
        songslist = intent.getSerializableExtra("LISTA") as ArrayList<AudioModel>?
        valor = intent.getSerializableExtra("VALOR") as Int
    }

    public override fun onStart() {
        super.onStart()

        //Session token abre a sessão do player
        val sessionToken =
                SessionToken(this, ComponentName(this,PlayerService::class.java))
        controllerFuture =
                MediaController.Builder(this, sessionToken).buildAsync()

        controllerFuture!!.addListener({
            // Controller serve literalmente pra isso
            //Ele controla a música
            try {
                playerView!!.player = controllerFuture!!.get()
                if (!Boomburst.is_playing) {
                    playerView!!.player!!.addMediaItems(pegarMusicas())
                    Boomburst.is_playing = true
                    musiquinha()
                } else if (playerView!!.player!!.currentMediaItemIndex != Boomburst.atual) {
                    musiquinha()
                } else {
                    telinha()
                }

                playerView!!.player!!.addListener(object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        super.onEvents(player, events)
                        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                            Boomburst.atual = player.currentMediaItemIndex
                            titulo!!.text = Objects.requireNonNull(player.currentMediaItem)!!.mediaMetadata.title
                        }
                    }
                })
            } catch (e: ExecutionException) {
                throw RuntimeException(e)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
        }, MoreExecutors.directExecutor())
    }

    private fun musiquinha() {
        playerView!!.player!!.seekTo(Boomburst.atual, 0)
        playerView!!.player!!.prepare()
        playerView!!.player!!.playWhenReady = true
        telinha()
    }

    private fun telinha() {
        titulo!!.text = Objects.requireNonNull(playerView!!.player!!.currentMediaItem)!!.mediaMetadata.title
    }


    //Pega todas as músicas da lista que foi jogada aqui
    //Não sei se vai funcionar 100% sabe
    //Maaaas torço pro ExoPlayer funcionar direitinho
    private fun pegarMusicas(): List<MediaItem> {
        val mediaitems: MutableList<MediaItem> = ArrayList()
        var novo: MediaItem
        val d = AppCompatResources.getDrawable(this,R.drawable.noivern_icon)
        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapdata = stream.toByteArray()
        for ((id, a) in songslist!!.withIndex()) {
            novo = MediaItem.Builder()
                    .setUri(a.path)
                    .setMediaId("id_$id")
                    .setMediaMetadata(MediaMetadata.Builder()
                            .setTitle(a.nome)
                            .maybeSetArtworkData(bitmapdata,MediaMetadata.PICTURE_TYPE_ILLUSTRATION)
                            .build())
                    .build()
            mediaitems.add(novo)
        }

        return mediaitems
    }
}
