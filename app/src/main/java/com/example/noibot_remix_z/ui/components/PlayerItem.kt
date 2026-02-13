package com.example.noibot_remix_z.ui.components

import com.example.noibot_remix_z.viewmodels.MusicPlayerViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import com.example.noibot_remix_z.R
import com.example.noibot_remix_z.ui.theme.primaryDark

@Composable
fun PlayerItem(
    modifier: Modifier = Modifier,
    backgroundcolor : Color,
    showSongName : Boolean = true,
    showProgress : Boolean = true,
    viewModel: MusicPlayerViewModel = viewModel(),

) {

    LaunchedEffect(Unit) {

    }



    val isPlaying : State<Boolean> = viewModel.isPlaying.collectAsState(initial = true)
    val loopState : State<Int> = viewModel.loopState.collectAsState()
    val shuffleState : State<Boolean> = viewModel.shuffleModeEnabled.collectAsState()
    val playbackState : State<Int> = viewModel.playbackState.collectAsState(initial = 0)
    val songName : State<MediaItem?> = viewModel.currentPlayingMediaItem.collectAsState()
//    val songLength : Long = viewModel.songLength.observeAsState(0L).value
//    val songPosition : Long = viewModel.songPosition.observeAsState(0L).value
//    val barProgress : Float = (songPosition.toFloat() / songLength.toFloat()) * 100

    val pauseplayButton =
        if(isPlaying.value)
            R.drawable.baseline_pause_circle_filled_24
        else
            R.drawable.baseline_play_circle_filled_24

    val shuffleButton =
        if(shuffleState.value)
            R.drawable.baseline_shuffle_on_24
        else
            R.drawable.baseline_no_shuffle_24

    val loopButton = when(loopState.value){
        0 -> R.drawable.baseline_no_repeat_24
        1 -> R.drawable.baseline_repeat_24
        2 -> R.drawable.baseline_repeat_one_24
        else -> R.drawable.baseline_no_repeat_24
    }


    Box(modifier = modifier
        .fillMaxWidth()
        .background(color = backgroundcolor)
        .padding(8.dp)
        .wrapContentHeight()
    ) {
        Column (
            modifier =
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(showSongName) {
                //Nome da musica
                Text(
                    text = songName.value?.mediaMetadata?.title.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    color = primaryDark,
                )
            }
//            if(showProgress){
//                WavySlider(
//                    value = barProgress,
//                    onValueChange = { },
//                )
//            }
            //Row com os botões de controle
            Row (modifier
                .wrapContentWidth()
                .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,


            ) {
                //Botão de loop
                Button(
                    onClick = { viewModel.toggleLoopState()},
                    modifier.wrapContentSize(),
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Transparent,
                        containerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Image(
                        painter =
                        painterResource(loopButton),
                        contentDescription = "",
                    )
                }

                //Botão de voltar
                Button(
                    onClick = { viewModel.skipToPrevious()},
                    modifier.wrapContentSize(),
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Transparent,
                        containerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Image(
                        painter =
                        painterResource(R.drawable.baseline_keyboard_arrow_left_24),
                        contentDescription = "",
                    )
                }

                //Botão de Pause/Play
                Button(
                    onClick = { viewModel.togglePlayPause()},
                    modifier.wrapContentSize(),
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Transparent,
                        containerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Image(
                        painter =
                        painterResource(pauseplayButton),
                        contentDescription = "",
                    )
                }

                //Botão de vai pra frente
                Button(
                    onClick = { viewModel.skipToNext()},
                    modifier.wrapContentSize(),
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Transparent,
                        containerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Image(
                        painter =
                        painterResource(R.drawable.baseline_keyboard_arrow_right_24),
                        contentDescription = "",
                    )
                }

                //Botão de shuffle
                Button(
                    onClick = { viewModel.toggleShuffleMode()},
                    modifier.wrapContentSize(),
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Transparent,
                        containerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Image(
                        painter =
                        painterResource(shuffleButton),
                        contentDescription = "",
                    )
                }


            }
        }

    }

}

@Preview(name = "PlayerItem")
@Composable
private fun PreviewPlayerItem() {
    PlayerItem(backgroundcolor = Color.Blue)
}