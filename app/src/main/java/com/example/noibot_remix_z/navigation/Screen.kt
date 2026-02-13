package com.example.noibot_remix_z.navigation

sealed class Screen(val route:String,val name:String){
    object nplaying : Screen(NavConst.NOW_PLAYING, "NowPlaying")
    object home : Screen(NavConst.HOME,"Home")
}