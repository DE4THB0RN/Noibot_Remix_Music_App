package com.example.noibot_remix_z.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.noibot_remix_z.ui.components.MusicItem
import com.example.noibot_remix_z.ui.theme.onPrimaryDark
import com.example.noibot_remix_z.viewmodels.MusicListViewModel

@Composable
fun SongsScreen(
    navController: NavHostController,
    musicListViewModel: MusicListViewModel = hiltViewModel()
) {
    val songs by musicListViewModel.songs.collectAsStateWithLifecycle()

    LazyColumn(
        Modifier
            .fillMaxSize(),
    ) {
        itemsIndexed(songs) { index, mediaItem ->
            MusicItem(index, mediaItem.mediaMetadata.title.toString(), navHostController = navController)
            if (index != songs.size - 1) {
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
