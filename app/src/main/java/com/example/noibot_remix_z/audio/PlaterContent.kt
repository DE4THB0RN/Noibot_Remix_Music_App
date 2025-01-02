package com.example.noibot_remix_z.audio

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import java.io.ByteArrayOutputStream
import com.example.noibot_remix_z.R


object PlaterContent {

    private var mediaitems: List<MediaItem> = listOf()
    private var listinha: List<AudioModel> = listOf()


    fun pegarLista() : List<MediaItem>{
        return mediaitems
    }

    fun pegarListinha() : List<AudioModel>{
        return listinha
    }

    fun pegarMusicas(context: Context, listinha : List<AudioModel>){
        this.listinha = listinha.toList()
        val mediaitems: MutableList<MediaItem> = ArrayList()
        var novo: MediaItem
        val d = AppCompatResources.getDrawable(context,R.drawable.noivern_icon)
        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapdata = stream.toByteArray()
        for ((id, a) in listinha.withIndex()) {
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

        this.mediaitems = mediaitems
    }
}