package com.example.noibotremix

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

//Principal:tomar cuidado ao mexer
class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var songless: TextView? = null
    private var listinha: ArrayList<AudioModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerview)
        songless = findViewById(R.id.nosongs)
        //O if que usa os dois mais abaixo NÃO MODIFICAR
        if (!permissao()) {
            permitir()
        }
        //Os abaixo servem pra pegar nome,dados e duração das músicas e puxar elas pro app(se forem musicas)
        val info = arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        )
        val selecionar = (MediaStore.Audio.Media.IS_MUSIC + " != 0 AND "
                + MediaStore.Audio.Media.DATA + " LIKE '%/Music/%'")
        var dados: AudioModel
        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, info, selecionar, null, null)
        while (cursor != null && cursor.moveToNext()) {
            dados = AudioModel(cursor.getString(1), cursor.getString(0))

            listinha.add(dados)
        }

        cursor?.close()

        //se não tem nada
        if (listinha.size == 0) {
            songless!!.visibility = View.VISIBLE
        } else {
            listinha.sortWith { a: AudioModel, b: AudioModel ->
                a.nome.compareTo(
                    b.nome,
                    ignoreCase = true
                )
            }
            recyclerView!!.setLayoutManager(LinearLayoutManager(this))
            recyclerView!!.setAdapter(Adaptador(listinha, applicationContext))
        }
    }

    //Isso aqui é pra ver se pode olhar o armazenamento
    private fun permissao(): Boolean {
        val resposta = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
        return resposta == PackageManager.PERMISSION_GRANTED
    }

    //Esse é pra implorar pela permissão se não tiver(com sorte eu não caio nessa[mas eu sei que vou])

    private fun permitir() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,Manifest.permission.READ_MEDIA_AUDIO)){
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

    override fun onResume() {
        super.onResume()
        if (recyclerView != null) {
            recyclerView!!.adapter = Adaptador(listinha, applicationContext)
        }
    }
}