package com.example.noibot_remix_z.audio.fontes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import androidx.appcompat.content.res.AppCompatResources
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.noibot_remix_z.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayOutputStream

class AudioLocal(@ApplicationContext val context: Context,
    ){
    private val selection = (MediaStore.Audio.Media.IS_MUSIC + " != 0 AND "
            + MediaStore.Audio.Media.DATA + " LIKE '%/Music/%'")
    private val sortOrder = MediaStore.Audio.Media.TITLE + " DESC"

fun getSongs(): MutableList<MediaItem>{
    val projection = arrayOf(
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DATA,
    )
    val listaAudios : MutableList<AudioModel> = mutableListOf()

    try {
        context.contentResolver?.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  projection, selection,null, sortOrder)
            .use { cursor ->
                while (cursor != null && cursor.moveToNext()){
                    val dados = AudioModel(cursor.getString(1), cursor.getString(0))
                    listaAudios.add(dados)
                }
                cursor?.close()
            }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    val mediaitems: MutableList<MediaItem> = ArrayList()
    var novo: MediaItem
        val d = AppCompatResources.getDrawable(context, R.drawable.noivern_icon)
        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapdata = stream.toByteArray()
    for ((id, a) in listaAudios.withIndex()) {
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