package com.example.noibot_remix_z.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable // This import IS present
import androidx.navigation.compose.NavHost // This IS present and correct
import com.example.noibot_remix_z.ui.screens.PlayerScreen
import com.example.noibot_remix_z.ui.screens.SongsScreen

@Composable
fun MyNavHostController(
    navHostController: NavHostController,
){

    NavHost(navController = navHostController, startDestination = Screen.home.route) {
        composable(Screen.home.route) { // This should work
            SongsScreen(navController = navHostController)
        }
        composable(Screen.nplaying.route) {
            PlayerScreen(navController = navHostController)
        }
        // Add other composable destinations here
    }
}