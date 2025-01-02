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
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.noibot_remix_z.audio.AudioModel
import com.example.noibot_remix_z.audio.PlaterContent
import com.example.noibot_remix_z.ui.theme.NoibotTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        if(!permissao() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permitir()
        }

        var listinha: ArrayList<AudioModel> = ArrayList()
        //Os abaixo servem pra pegar nome,dados e duração das músicas e puxar elas pro app(se forem musicas)
        val info = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA
        )
        val selecionar = (MediaStore.Audio.Media.IS_MUSIC + " != 0 AND "
                + MediaStore.Audio.Media.DATA + " LIKE '%/Music/%'")
        var dados: AudioModel
        val cursor =  contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, info, selecionar, null, null)
        while (cursor != null && cursor.moveToNext()) {
            dados = AudioModel(cursor.getString(1), cursor.getString(0))

            listinha.add(dados)
        }

        cursor?.close()

        if (listinha.isNotEmpty()){
            listinha.sortWith{
                    a: AudioModel,
                    b : AudioModel ->
                a.nome.compareTo(b.nome, ignoreCase = true
                )
            }
        }

        PlaterContent.pegarMusicas(this,listinha)

        enableEdgeToEdge()
        setContent {
            NoibotTheme {
                NoibotApp(listinha)
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    NoibotTheme {
        NoibotApp(listOf())
    }
}