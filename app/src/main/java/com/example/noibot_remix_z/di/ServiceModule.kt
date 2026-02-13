package com.example.noibot_remix_z.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @Provides
    @ServiceScoped
    fun proverAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    @ServiceScoped
    fun proverExoPlayer(@ApplicationContext context: Context, audioAttributes: AudioAttributes) =
        ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true)
            .setAudioAttributes(audioAttributes,true)
            .build()

    @Provides
    @ServiceScoped
    fun proverDataSourceFactory(@ApplicationContext context: Context ) =
        DefaultDataSource.Factory(context)


}