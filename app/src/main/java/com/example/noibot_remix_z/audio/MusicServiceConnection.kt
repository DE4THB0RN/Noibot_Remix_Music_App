package com.example.noibot_remix_z.audio

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

// Assume you have a MusicService that is a MediaSessionService


@Singleton // Make this a singleton so only one connection exists
class MusicServiceConnection @Inject constructor(
    private val context: Context,
    // If you have a specific CoroutineScope for your application, inject it
    // otherwise, create one.
    // private val externalScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {
    // If MusicService is in a different module, ensure it's accessible.
    // Replace com.example.noibot_remix_z.service.MusicService with your actual service path.
    // You MUST have a MediaSessionService defined for this to work.
    // For this example, let's assume it's:
    // package com.example.noibot_remix_z.service
    // class MusicService : MediaSessionService() { /* ... */ }

    private val serviceComponent by lazy {
        ComponentName(context, "com.example.noibot_remix_z.audio.MediaService")
    }

    private var controllerFuture: ListenableFuture<MediaController>? = null
    var mediaController: MediaController? = null
        private set

    private val _loopState = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val loopState: StateFlow<Int> = _loopState.asStateFlow()

    private val _shuffleModeEnabled = MutableStateFlow(false)
    val shuffleModeEnabled: StateFlow<Boolean> = _shuffleModeEnabled.asStateFlow()



    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _playbackState = MutableStateFlow<@Player.State Int>(Player.STATE_IDLE)
    val playbackState: StateFlow<@Player.State Int> = _playbackState.asStateFlow()

    private val _currentPlayingMediaItem = MutableStateFlow<MediaItem?>(null)
    val currentPlayingMediaItem: StateFlow<MediaItem?> = _currentPlayingMediaItem.asStateFlow()

    private val _currentPlayingMediaMetadata = MutableStateFlow<MediaMetadata?>(null)
    val currentPlayingMediaMetadata: StateFlow<MediaMetadata?> = _currentPlayingMediaMetadata.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main) // Use Main for UI updates

    private var playerListener: Player.Listener? = null



    init {
        connect()
    }

    fun connect() {
        if (mediaController != null && mediaController?.isConnected == true) {
            _isConnected.value = true
            return // Already connected
        }

        val sessionToken = SessionToken(context, serviceComponent)
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture?.addListener({
            try {
                val controller = controllerFuture?.get() // This can throw if connection failed
                if (controller != null && controller.isConnected) {
                    mediaController = controller
                    _isConnected.value = true
                    registerPlayerListener()
                    // Update initial state from the controller
                    _playbackState.value = controller.playbackState
                    _currentPlayingMediaItem.value = controller.currentMediaItem
                    _currentPlayingMediaMetadata.value = controller.mediaMetadata
                    _isPlaying.value = controller.isPlaying
                    _shuffleModeEnabled.value = controller.shuffleModeEnabled
                    _loopState.value = controller.repeatMode
                } else {
                    _isConnected.value = false
                    // Handle connection failure, e.g., retry or log
                }
            } catch (e: Exception) {
                _isConnected.value = false
                mediaController = null
                Log.e("MusicServiceConnection", "Error connecting to MediaController", e)
            }
        }, MoreExecutors.directExecutor()) // Use directExecutor for listeners on ListenableFuture
    }

    private fun registerPlayerListener() {
        playerListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                _playbackState.value = playbackState
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _currentPlayingMediaItem.value = mediaItem
                _currentPlayingMediaMetadata.value = mediaItem?.mediaMetadata
            }

            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                _currentPlayingMediaMetadata.value = mediaMetadata
                // If mediaItem didn't change but metadata did (e.g., artwork loaded)
                //if (_currentPlayingMediaItem.value?.mediaId == mediaMetadata.id) {
                  //  _currentPlayingMediaItem.value = _currentPlayingMediaItem.value?.buildUpon()
                    //    ?.setMediaMetadata(mediaMetadata)
                    //    ?.build()
                //}
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                _shuffleModeEnabled.value = shuffleModeEnabled
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
                _loopState.value = repeatMode
            }
        }
        mediaController?.addListener(playerListener!!)
    }

    fun release() {
        _isConnected.value = false
        playerListener?.let { mediaController?.removeListener(it) }
        playerListener = null
        controllerFuture?.let { future ->
            MediaController.releaseFuture(future)
            controllerFuture = null
        }
        mediaController = null // Explicitly nullify
        // Consider cancelling the coroutineScope if this connection is truly being destroyed
        // and not just temporarily disconnected.
        // coroutineScope.cancel()
    }

    // --- Media Controller Delegate Methods ---
    // These allow your UI/ViewModel to interact with the player via this connection class

    fun play() {
        mediaController?.play()
    }

    fun pause() {
        mediaController?.pause()
    }

    fun seekToNext() {
        mediaController?.seekToNextMediaItem()
    }

    fun seekToPrevious() {
        mediaController?.seekToPreviousMediaItem()
    }

    fun seekTo(positionMs: Long) {
        mediaController?.seekTo(positionMs)
    }

    fun setMediaItems(mediaItems: List<MediaItem>, autoPlay: Boolean = true) {
        mediaController?.setMediaItems(mediaItems)
        if (autoPlay) {
            mediaController?.prepare()
            mediaController?.play()
        } else {
            mediaController?.prepare()
        }
    }

    fun addMediaItem(mediaItem: MediaItem) {
        mediaController?.addMediaItem(mediaItem)
    }

    fun addMediaItem(index: Int, mediaItem: MediaItem) {
        mediaController?.addMediaItem(index, mediaItem)
    }

    fun togglePlayPause(){
        if(mediaController?.isPlaying == true)
            mediaController?.pause()
        else
            mediaController?.play()
    }

    fun toggleLoopState(){
        when(_loopState.value){
            Player.REPEAT_MODE_OFF -> mediaController?.repeatMode = Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> mediaController?.repeatMode = Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> mediaController?.repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    fun toggleShuffleMode(){
        mediaController?.shuffleModeEnabled = !mediaController?.shuffleModeEnabled!!
    }

    // --- Example of getting current position (can be done via a Flow in ViewModel) ---
    val currentPosition: Long
        get() = mediaController?.currentPosition ?: 0L

    val duration: Long
        get() = mediaController?.duration ?: 0L

}