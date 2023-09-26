package com.example.noibotremix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {

    ArrayList<AudioModel> songlist;
    Context context;

    public Adaptador(ArrayList<AudioModel> songlist, Context context) {
        this.songlist = songlist;
        this.context = context;
    }
    //faz uma lista pra valer com o numero de musicas
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new Adaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adaptador.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
       AudioModel dados = songlist.get(position);
       holder.nomes.setText(dados.getNome());

       if (Boomburst.atual == position){
           holder.nomes.setTextColor(Color.parseColor("#b03b26"));
       }
       else
       {
           holder.nomes.setTextColor(Color.parseColor("#ffffff"));
       }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Boomburst.getround().reset();
               Boomburst.atual= position;
               Intent intent = new Intent(context,Player.class);
               intent.putExtra("LISTA",songlist);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);

           }
       });
    }
    //pega o numero de musicas
    @Override
    public int getItemCount() {
        return songlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView nomes;
        ImageView icon;
        public ViewHolder(View itemView){
            super(itemView);
            nomes = itemView.findViewById(R.id.nome_musica);
            icon = itemView.findViewById(R.id.icone);

        }

    }
}
