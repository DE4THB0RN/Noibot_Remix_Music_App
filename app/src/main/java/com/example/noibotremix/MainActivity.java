package com.example.noibotremix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

//Principal:tomar cuidado ao mexer
public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView songless;
    ArrayList<AudioModel> listinha = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        songless = findViewById(R.id.nosongs);
        //O if que usa os dois mais abaixo NÃO MODIFICAR
        if (Permissao()== false){
            Permitir();
            return;
        }
        //Os abaixo servem pra pegar nome,dados e duração das músicas e puxar elas pro app(se forem musicas)
        String[] info = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String selecionar = MediaStore.Audio.Media.IS_MUSIC +" != 0 ";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,info,selecionar,null,null);
        while (cursor.moveToNext()){
            AudioModel dados = new AudioModel(cursor.getString(1),cursor.getString(0),cursor.getString(2));
            if (new File(dados.getPath()).exists())
            {
                listinha.add(dados);

            }
        }
        //se não tem nada
        if (listinha.size()==0){
            songless.setVisibility(View.VISIBLE);
        }
        //se tem coisa
        else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new Adaptador(listinha,getApplicationContext()));

        }
    }

    //Isso aqui é pra ver se pode olhar o armazenamento
    boolean Permissao(){
      int resposta = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
      if(resposta == PackageManager.PERMISSION_GRANTED){
          return true;
      }
      else{
          return false;
      }
    }
    //Esse é pra implorar pela permissão se não tiver(com sorte eu não caio nessa[mas eu sei que vou])
    void Permitir(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"Preciso da permissão pra funcionar sabe,ativa ela lá",Toast.LENGTH_SHORT).show();
        }
        else{
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null){
            recyclerView.setAdapter(new Adaptador(listinha,getApplicationContext()));
        }
    }
}