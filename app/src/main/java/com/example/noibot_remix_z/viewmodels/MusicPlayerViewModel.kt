package com.example.noibot_remix_z.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.example.noibot_remix_z.audio.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = musicServiceConnection.isConnected
    val playbackState: StateFlow<Int> = musicServiceConnection.playbackState
    val currentPlayingMediaItem: StateFlow<MediaItem?> = musicServiceConnection.currentPlayingMediaItem
    val currentPlayingMediaMetadata: StateFlow<MediaMetadata?> = musicServiceConnection.currentPlayingMediaMetadata
    val isPlaying: StateFlow<Boolean> = musicServiceConnection.isPlaying
    val loopState: StateFlow<Int> = musicServiceConnection.loopState
    val shuffleModeEnabled: StateFlow<Boolean> = musicServiceConnection.shuffleModeEnabled

    // Nome da música atual
    val songName: StateFlow<String> = currentPlayingMediaMetadata
        .map { metadata ->
            metadata?.title?.toString() ?: ""
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    // Duração da música
    val durationMs: StateFlow<Long> = musicServiceConnection.playbackState
        .map {
            musicServiceConnection.duration
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0L)

    // Alias para manter compatibilidade
    val songLength: StateFlow<Long> = durationMs

    // Posição atual com atualização periódica
    val currentPositionMs: StateFlow<Long> = flow {
        while (true) {
            if (isPlaying.value) {
                emit(musicServiceConnection.currentPosition)
            } else {
                emit(musicServiceConnection.currentPosition)
            }
            delay(1.seconds)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0L)

    // Alias para manter compatibilidade
    val songPosition: StateFlow<Long> = currentPositionMs

    fun toggleLoopState() {
        musicServiceConnection.toggleLoopState()
    }

    fun togglePlayPause() {
        musicServiceConnection.togglePlayPause()
    }

    // Alias para manter compatibilidade
    fun togglePlay() {
        togglePlayPause()
    }

    fun toggleShuffleMode() {
        musicServiceConnection.toggleShuffleMode()
    }

    // Alias para manter compatibilidade
    fun shuffleSongs() {
        toggleShuffleMode()
    }

    // Alias para manter compatibilidade
    fun toggleLoop() {
        toggleLoopState()
    }

    fun play() {
        musicServiceConnection.play()
    }

    fun pause() {
        musicServiceConnection.pause()
    }

    fun skipToNext() {
        musicServiceConnection.seekToNext()
    }

    // Alias para manter compatibilidade
    fun nextItem() {
        skipToNext()
    }

    fun skipToPrevious() {
        musicServiceConnection.seekToPrevious()
    }

    // Alias para manter compatibilidade
    fun previousItem() {
        skipToPrevious()
    }

    fun seekTo(positionMs: Long) {
        musicServiceConnection.seekTo(positionMs)
    }

    fun loadAndPlayPlaylist(items: List<MediaItem>) {
        if (musicServiceConnection.isConnected.value) {
            musicServiceConnection.setMediaItems(items, autoPlay = true)
        } else {
            // Handle case where service is not yet connected (e.g., queue the action)
        }
    }

    // Função para tocar uma música específica por índice
    fun tocarMusica(id: Int) {
        viewModelScope.launch {
            if (musicServiceConnection.mediaController?.currentMediaItemIndex != id) {
                musicServiceConnection.mediaController?.seekTo(id, 0)
                musicServiceConnection.mediaController?.prepare()
                musicServiceConnection.mediaController?.playWhenReady = true
            }
        }
    }

    // Função para adicionar um item à playlist
    fun addMediaItem(mediaItem: MediaItem) {
        musicServiceConnection.addMediaItem(mediaItem)
    }

    // Função para adicionar um item em um índice específico
    fun addMediaItem(index: Int, mediaItem: MediaItem) {
        musicServiceConnection.addMediaItem(index, mediaItem)
    }

    // It's good practice to ensure connection is released when ViewModel is cleared,
    // although with a Singleton MusicServiceConnection, it might live longer.
    // However, if the connection logic itself is tied to UI lifecycle, consider this.
    // override fun onCleared() {
    //     super.onCleared()
    //     // If MusicServiceConnection is not a Singleton or should be released with ViewModel
    //     // musicServiceConnection.release()
    // }
}