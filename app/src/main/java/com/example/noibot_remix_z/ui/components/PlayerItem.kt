package com.example.noibot_remix_z.ui.components

import androidx.compose.runtime.getValue
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noibot_remix_z.viewmodels.PlayerViewModel
import com.example.noibot_remix_z.R
import com.example.noibot_remix_z.ui.theme.primaryDark
import ir.mahozad.multiplatform.wavyslider.material3.WavySlider

@Composable
fun PlayerItem(
    backgroundcolor : Color,
    showSongName : Boolean = true,
    showProgress : Boolean = true,
    viewModel: PlayerViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    LaunchedEffect(Unit) {

    }

    val isPlaying : Boolean = viewModel.isPlaying.observeAsState().value == true
    val loopState : Int = viewModel.loopState.observeAsState(0).value
    val shuffleState : Boolean = viewModel.shuffleState.observeAsState(false).value
    var playbackState : Int = viewModel.playbackState.observeAsState(0).value
    val songName : String = viewModel.songName.observeAsState("").value
//    val songLength : Long = viewModel.songLength.observeAsState(0L).value
//    val songPosition : Long = viewModel.songPosition.observeAsState(0L).value
//    val barProgress : Float = (songPosition.toFloat() / songLength.toFloat()) * 100

    val pauseplayButton =
        if(isPlaying)
            R.drawable.baseline_pause_circle_filled_24
        else
            R.drawable.baseline_play_circle_filled_24

    val shuffleButton =
        if(shuffleState)
            R.drawable.baseline_shuffle_on_24
        else
            R.drawable.baseline_no_shuffle_24

    val loopButton = when(loopState){
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
                    text = songName,
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
                    onClick = { viewModel.toggleLoop()},
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
                    onClick = { viewModel.previousItem()},
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
                    onClick = { viewModel.togglePlay()},
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
                    onClick = { viewModel.nextItem()},
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
                    onClick = { viewModel.shuffleSongs()},
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