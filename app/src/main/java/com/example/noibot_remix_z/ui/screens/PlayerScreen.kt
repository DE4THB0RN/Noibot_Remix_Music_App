package com.example.noibot_remix_z.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noibot_remix_z.R
import com.example.noibot_remix_z.ui.components.PlayerItem
import com.example.noibot_remix_z.ui.theme.primaryContainerDark

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier
) {

    Column (modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {

        Image(
            painter = painterResource(R.drawable.noivern_invisblis),
            contentDescription = "",
        )

        Spacer(modifier = Modifier.height(250.dp))

        PlayerItem(backgroundcolor = primaryContainerDark, showSongName = true)

    }

}

@Preview(name = "PlayerScreen")
@Composable
private fun PreviewPlayerScreen() {
    PlayerScreen()
}