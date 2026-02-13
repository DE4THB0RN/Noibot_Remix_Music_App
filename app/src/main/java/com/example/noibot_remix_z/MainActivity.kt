package com.example.noibot_remix_z

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noibot_remix_z.audio.fontes.AudioModel
import com.example.noibot_remix_z.ui.components.NoibotAppBar
import com.example.noibot_remix_z.ui.screens.PlayerScreen
import com.example.noibot_remix_z.ui.screens.SongsScreen
import com.example.noibot_remix_z.ui.theme.NoibotTheme
import com.example.noibot_remix_z.ui.theme.primaryContainerDark
import com.example.noibot_remix_z.ui.theme.primaryDark
import com.example.noibot_remix_z.ui.theme.secondaryDark
import dagger.hilt.android.AndroidEntryPoint

enum class Telas{
    Songs,
    Player
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!permissao() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permitir()
        }

        enableEdgeToEdge()
        setContent {
            NoibotTheme {
                NoibotApp()
            }
        }
    }

    //Isso aqui é pra ver se pode olhar o armazenamento
    private fun permissao(): Boolean {
        val resposta = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
        return resposta == PackageManager.PERMISSION_GRANTED
    }

    //Esse é pra implorar pela permissão se não tiver(com sorte eu não caio nessa[mas eu sei que vou])

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun permitir() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.READ_MEDIA_AUDIO)){
            Toast.makeText(this@MainActivity, "Preciso da permissão pra funcionar sabe,ativa ela lá", Toast.LENGTH_SHORT).show()
        }
        else{
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.READ_MEDIA_AUDIO), 123)
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS)) {
            Toast.makeText(this@MainActivity, "Preciso da permissão pra funcionar sabe,ativa ela lá", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 123)
        }
    }

    @Composable
    private fun MainUI(modifier : Modifier = Modifier){
        val navController = rememberNavController()

        Scaffold(
            topBar = {
                NoibotAppBar()
            }
        ) {
                innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Telas.Songs.name,
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ){
                composable(route = Telas.Songs.name){
                    SongsScreen(navController = navController)
                }
                composable(route = Telas.Player.name){
                    PlayerScreen(navController = navController)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    NoibotTheme {
        NoibotApp()
    }
}