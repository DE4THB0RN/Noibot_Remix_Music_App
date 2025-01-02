package com.example.noibot_remix_z.ui.screens

import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.noibot_remix_z.audio.AudioModel
import com.example.noibot_remix_z.audio.Boomburst
import com.example.noibot_remix_z.ui.components.MusicItem
import com.example.noibot_remix_z.ui.theme.onPrimaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongsScreen(
    listinha: List<AudioModel>,
    modifier: Modifier = Modifier.Companion,
    navhostController : NavHostController
    ) {

    LazyColumn(
        Modifier
            .fillMaxSize(),
        ) {
            itemsIndexed(listinha){ index,d ->
                MusicItem(index,d.nome,navHostController = navhostController)
                if (index != listinha.size - 1){
                    HorizontalDivider(color = onPrimaryDark)
                }

            }

        }

}

@Preview(name = "SongsScreen")
@Composable
private fun PreviewSongsScreen() {
  //  SongsScreen(listinha = listOf(AudioModel("Musica 1", "Caminho 1"), AudioModel("Musica 2", "Caminho 2")))
}