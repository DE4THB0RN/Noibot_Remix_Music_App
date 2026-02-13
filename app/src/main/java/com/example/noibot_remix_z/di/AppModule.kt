package com.example.noibot_remix_z.di

import android.content.Context
import com.example.noibot_remix_z.audio.fontes.AudioLocal
import com.example.noibot_remix_z.audio.fontes.MusicasLocais
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun proverAudioLocal(@ApplicationContext context: Context) = AudioLocal(context)

    @Singleton
    @Provides
    fun proverMediaItems(musicasLocais: MusicasLocais) = musicasLocais.pegarMusicas()

}