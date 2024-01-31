package com.example.noibotremix;

import java.io.Serializable;

public class AudioModel implements Serializable {
    String path;
    String nome;
    String duração;

    public AudioModel(String path, String nome, String duração) {
        this.path = path;
        this.nome = nome;
        this.duração = duração;
    }

    public AudioModel(String path,String nome){
        this.path = path;
        this.nome = nome;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDuração() {
        return duração;
    }

    public void setDuração(String duração) {
        this.duração = duração;
    }
}
