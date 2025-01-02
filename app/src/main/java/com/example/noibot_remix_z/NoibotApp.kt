package com.example.noibot_remix_z

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noibot_remix_z.audio.AudioModel
import com.example.noibot_remix_z.audio.Boomburst
import com.example.noibot_remix_z.ui.components.PlayerItem
import com.example.noibot_remix_z.ui.screens.PlayerScreen
import com.example.noibot_remix_z.ui.screens.SongsScreen
import com.example.noibot_remix_z.ui.theme.primaryContainerDark
import com.example.noibot_remix_z.ui.theme.primaryDark



enum class Telas{
    Songs,
    Player
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoibotAppBar(){
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = primaryContainerDark,
            scrolledContainerColor = primaryContainerDark,
            navigationIconContentColor = primaryDark,
            titleContentColor = primaryDark,
            actionIconContentColor = primaryDark
        ),
        title = {
            Text(text = "Noibot Re:Mix Z")
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoibotApp(
    listinha: List<AudioModel> = listOf(),
    modifier: Modifier = Modifier,
    navhostController: NavHostController = rememberNavController()
) {

    Scaffold (
        topBar = {
            NoibotAppBar()
        },

    ) { innerPadding ->
        NavHost(
            navController = navhostController,
            startDestination = Telas.Songs.name,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            composable(route = Telas.Songs.name){
                SongsScreen(listinha = listinha, navhostController = navhostController)
            }
            composable(route = Telas.Player.name){
                PlayerScreen()
            }
        }
    }
}

@Preview(name = "NoibotApp")
@Composable
private fun PreviewNoibotApp() {
    NoibotApp()
}