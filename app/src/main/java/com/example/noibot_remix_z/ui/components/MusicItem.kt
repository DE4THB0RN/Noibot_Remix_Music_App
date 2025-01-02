package com.example.noibot_remix_z.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.noibot_remix_z.R
import com.example.noibot_remix_z.Telas
import com.example.noibot_remix_z.audio.Boomburst
import com.example.noibot_remix_z.ui.theme.errorContainerDark

@Composable
fun MusicItem(
    id : Int,
    nomeMusica : String,
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {

    val noib1 = painterResource(id = R.drawable.noibat_b)
    val noib2 = painterResource(id = R.drawable.noibat_shiny_b)

    var noibatIcon = if(id == Boomburst.atual) noib2 else noib1
    var textColor = if(id == Boomburst.atual) errorContainerDark else MaterialTheme.typography.bodyMedium.color

    Row (modifier.fillMaxWidth()) {
        Card(
            onClick = {
                        Boomburst.atual = id
                        navHostController.navigate(Telas.Player.name)
                      } ,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(6.dp, 12.dp)

        ) {

            ListItem(headlineContent = {
                Text(
                    text= nomeMusica,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = textColor
                    )
            },
                leadingContent = {
                    Image(
                        painter = noibatIcon,
                        contentDescription = "",
                        modifier = Modifier.size(36.dp)
                    )
                }
            )
        }
    }
}

@Preview(name = "MusicItem")
@Composable
private fun PreviewMusicItem() {
    MusicItem(0,"Musica", navHostController =  rememberNavController())
}