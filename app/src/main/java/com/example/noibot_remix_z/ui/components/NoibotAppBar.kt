package com.example.noibot_remix_z.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import com.example.noibot_remix_z.ui.theme.primaryContainerDark
import com.example.noibot_remix_z.ui.theme.primaryDark
import com.example.noibot_remix_z.ui.theme.secondaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoibotAppBar(){
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = primaryContainerDark,
            scrolledContainerColor = primaryContainerDark,
            navigationIconContentColor = primaryDark,
            titleContentColor = primaryDark,
            actionIconContentColor = primaryDark,
            subtitleContentColor = secondaryDark
        ),
        title = {
            Text(text = "Noibot Re:Mix Z")
        }
    )
}