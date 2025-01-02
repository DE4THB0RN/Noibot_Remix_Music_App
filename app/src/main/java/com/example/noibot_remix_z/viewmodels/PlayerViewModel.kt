package com.example.noibot_remix_z.viewmodels

import android.app.Application
import android.content.ComponentName
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.noibot_remix_z.audio.Boomburst
import com.example.noibot_remix_z.audio.PlaterContent
import com.example.noibot_remix_z.audio.PlayerService
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class PlayerViewModel(app: Application) : AndroidViewModel(app) {
    private val _isPlaying = MutableLiveData(false)
    private val playerReady = MutableStateFlow(false)
    private var _player : MediaController? = null
    private val _playbackState = MutableLiveData(0)
    private val _songLength = MutableLiveData(0L)
    private val _songPosition = MutableLiveData(0L)
    private val _loopState = MutableLiveData(0)
    private val _shuffleState = MutableLiveData(false)
    private val _songName = MutableLiveData("")
    val songName : LiveData<String> = _songName
    val playbackState : LiveData<Int> = _playbackState
    val loopState : LiveData<Int> = _loopState
    val songLength : LiveData<Long> = _songLength
    val songPosition : LiveData<Long> = _songPosition
    val shuffleState : LiveData<Boolean> = _shuffleState
    val isPlaying : LiveData<Boolean> = _isPlaying


    init {

        val mediaitems: List<MediaItem> = PlaterContent.pegarLista()
        val sessionToken =
            SessionToken(app, ComponentName(app, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(app,sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                _player = controllerFuture.get()

                //Setando MediaItems
                if(_player?.mediaItemCount == 0){
                    _player?.setMediaItems(mediaitems)
                }

                if(!Boomburst.is_playing) Boomburst.is_playing = true

                playerReady.value = true
                _isPlaying.value = Boomburst.is_playing == true
                tocarMusica(Boomburst.atual)


//                viewModelScope.launch{
//                    _player?.currentPositionFlow()?.collect(){
//                        currentPosition -> _songPosition.value = currentPosition.toLong(DurationUnit.SECONDS)
//                        }
//                }

                _player?.addListener(object : Player.Listener {

                    override fun onPlaybackStateChanged(playbackStater: Int) {
                        _playbackState.value = playbackStater
                    }


                    override fun onEvents(player: Player, events: Player.Events) {
                        super.onEvents(player, events)
                        if(events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)){
                            Boomburst.atual = _player?.currentMediaItemIndex ?: -1
                            _songName.value = (_player?.currentMediaItem?.mediaMetadata?.title ?: "").toString()
                            _songLength.value = _player?.duration ?: 0
                        }
                        if(events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)){
                            _isPlaying.value = _player?.isPlaying == true
                        }
                    }
                })
            },
            MoreExecutors.directExecutor()

        )
    }

    fun togglePlay() = if (_isPlaying.value == true) pause() else play()

    fun tocarMusica(id: Int) = viewModelScope.launch {

        if(_player?.currentMediaItemIndex != id){
            _player?.seekTo(id,0)
            _player?.prepare()
            _player?.playWhenReady = true

        }
        _songName.value = (_player?.currentMediaItem?.mediaMetadata?.title ?: "").toString()
        _songLength.value = _player?.duration ?: 0
    }

    fun shuffleSongs() {
        _player?.shuffleModeEnabled = !_player?.shuffleModeEnabled!!
        _shuffleState.value = _player?.shuffleModeEnabled == true
    }

    fun toggleLoop(){
        if(_player?.repeatMode == Player.REPEAT_MODE_OFF){
            _player?.repeatMode = Player.REPEAT_MODE_ONE
            _loopState.value = 2
        }
        else if(_player?.repeatMode == Player.REPEAT_MODE_ONE){
            _player?.repeatMode = Player.REPEAT_MODE_ALL
            _loopState.value = 1
        }
        else{
            _player?.repeatMode = Player.REPEAT_MODE_OFF
            _loopState.value = 0
        }
    }


    fun previousItem() {
        _player?.seekToPreviousMediaItem()
    }

    fun nextItem() {
        _player?.seekToNextMediaItem()
    }

    private fun play() {
        _isPlaying.value = true
        _player?.play()
    }

    private fun pause(){
        _isPlaying.value = false
        _player?.pause()
    }

    override fun onCleared() {
        super.onCleared()
        _player?.release()
//        val intent = Intent(getApplication(), PlayerService::class.java)
//        getApplication<Application>().stopService(intent)
    }

    private fun seekTo(newPos : Long){
        _player?.seekTo(newPos)
    }

    fun Player.currentPositionFlow() = flow {
        val updateFrequency = 1.seconds
        while(true){
            if(_isPlaying.value == true){
                emit(currentPosition.toDuration(DurationUnit.SECONDS))
            }
            delay(updateFrequency)
        }
    }.flowOn(Dispatchers.Main)


}