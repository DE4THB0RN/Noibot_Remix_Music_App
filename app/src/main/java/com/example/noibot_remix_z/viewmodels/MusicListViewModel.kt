package com.example.noibot_remix_z.viewmodels

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.noibot_remix_z.audio.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MusicListViewModel @Inject constructor(
    private val mediaItems: MutableList<MediaItem>
) : ViewModel() {
    private val _songs = MutableStateFlow<List<MediaItem>>(emptyList())
    val songs = _songs.asStateFlow()

    init {
        _songs.value = mediaItems
    }
}
