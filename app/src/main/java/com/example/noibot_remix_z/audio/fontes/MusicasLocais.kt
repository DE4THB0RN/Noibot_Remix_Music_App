package com.example.noibot_remix_z.audio.fontes

import androidx.media3.common.MediaItem
import javax.inject.Inject

class MusicasLocais @Inject constructor( private val audioLocal: AudioLocal) {

    fun pegarMusicas(): MutableList<MediaItem> = audioLocal.getSongs()
}