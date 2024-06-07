package com.example.noibotremix

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adaptador(private var songlist: ArrayList<AudioModel>, private var context: Context) : RecyclerView.Adapter<Adaptador.ViewHolder>() {
    //faz uma lista pra valer com o numero de musicas
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val dados = songlist[position]
        holder.nomes.text = dados.nome

        if (Boomburst.atual == position) {
            holder.nomes.setTextColor(Color.parseColor("#b03b26"))
            holder.icon.setImageResource(R.drawable.noibat_shiny)
        } else {
            holder.icon.setImageResource(R.drawable.noibat_a)
            holder.nomes.setTextColor(Color.parseColor("#ffffff"))
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlayerX::class.java)
            if (Boomburst.atual != position) {
                Boomburst.atual = position
                intent.putExtra("VALOR", 0)
            } else {
                intent.putExtra("VALOR", 1)
            }

            intent.putExtra("LISTA", songlist)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    //pega o numero de musicas
    override fun getItemCount(): Int {
        return songlist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nomes: TextView = itemView.findViewById(R.id.nome_musica)
        var icon: ImageView = itemView.findViewById(R.id.icone)
    }
}
